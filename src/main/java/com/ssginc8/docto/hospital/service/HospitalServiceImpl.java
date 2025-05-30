package com.ssginc8.docto.hospital.service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.doctor.entity.Doctor;

import com.ssginc8.docto.doctor.repo.DoctorRepo;
import com.ssginc8.docto.doctor.repo.DoctorScheduleRepo;

import com.ssginc8.docto.hospital.dto.HospitalRequest;
import com.ssginc8.docto.hospital.dto.HospitalResponse;
import com.ssginc8.docto.hospital.dto.HospitalReviewResponse;
import com.ssginc8.docto.hospital.dto.HospitalScheduleRequest;
import com.ssginc8.docto.hospital.dto.HospitalScheduleResponse;
import com.ssginc8.docto.hospital.dto.HospitalUpdate;
import com.ssginc8.docto.hospital.dto.HospitalWaiting;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.entity.HospitalSchedule;
import com.ssginc8.docto.hospital.entity.ProvidedService;
import com.ssginc8.docto.hospital.provider.HospitalProvider;
import com.ssginc8.docto.hospital.repo.HospitalRepo;
import com.ssginc8.docto.hospital.repo.HospitalScheduleRepo;
import com.ssginc8.docto.hospital.repo.ProvidedServiceRepo;
import com.ssginc8.docto.review.dto.ReviewAllListResponse;
import com.ssginc8.docto.review.provider.ReviewProvider;
import com.ssginc8.docto.review.repository.ReviewRepo;
import com.ssginc8.docto.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

	private final HospitalProvider hospitalProvider;

	private final HospitalRepo hospitalRepo;

	private final ProvidedServiceRepo providedServiceRepo;
	private final HospitalScheduleRepo hospitalScheduleRepo;
	private final DoctorRepo doctorRepo;
	private final DoctorScheduleRepo doctorScheduleRepo;

	private final ReviewProvider reviewProvider;

	/**
	 *  위치기반 병원 리스트 조회
	 */
	// @Override
	// public Page<HospitalNameDTO> getHospitalsWithinRadius(double lat, double lng, double radius, Pageable pageable) {
	// 	//로직추가하자
	// 	Page<Hospital> hospitals = hospitalRepository.findWithinRadius(lat, lng, radius, pageable);
	// 	return hospitals.map(hospital -> new HospitalNameDTO(hospital.getId(), hospital.getName()));
	//
	// 	return hospitalRepository.findHospitalsWithinRadius(lat, lng, radius)
	// 		.stream()
	// 		.map(HospitalNameDTO::new)
	// 		.collect(Collectors.toList());
	// }

	/**
	 * 병원아이디 상세 조회
	 *
	 */
	@Override
	public HospitalResponse getHospitalId(Long hospitalId) {
		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		// 병원 ID로 서비스 목록 조회 (단방향으로 수정된 구조)
		List<ProvidedService> services = providedServiceRepo.findByHospitalHospitalId(hospitalId);

		String serviceNames = services == null || services.isEmpty()
			? null
			: services.stream()
			.map(ProvidedService::getServiceName)
			.collect(Collectors.joining(", "));

		return HospitalResponse.builder()
			.hospitalId(hospital.getHospitalId())
			.userId(hospital.getUser() != null ? hospital.getUser().getUserId() : null)
			.name(hospital.getName())
			.address(hospital.getAddress())
			.latitude(hospital.getLatitude())
			.longitude(hospital.getLongitude())
			.phone(hospital.getPhone())
			.introduction(hospital.getIntroduction())
			.notice(hospital.getNotice())
			.waiting(hospital.getWaiting())
			.businessRegistrationNumber(hospital.getBusinessRegistrationNumber())
			.serviceNames(Collections.singletonList(serviceNames))
			.build();
	}


	/**
	 * 병원 정보 등록
	 *
	 */
	@Override
	public Long saveHospital(HospitalRequest hospitalRequest) {
		// 유저 조회 (null 검사 포함된 내부 구현을 권장)
		User user = hospitalProvider.getUserById(hospitalRequest.getUserId());

		// 병원 엔티티 생성
		Hospital hospital = Hospital.create(
			user,
			hospitalRequest.getName(),
			hospitalRequest.getAddress(),
			hospitalRequest.getPhone(),
			hospitalRequest.getIntroduction(),
			hospitalRequest.getLongitude(),
			hospitalRequest.getLatitude(),
			hospitalRequest.getNotice(),
			hospitalRequest.getBusinessRegistrationNumber()
		);

		// 먼저 병원 저장 → ID 생성됨
		hospitalProvider.saveHospital(hospital);


		// 서비스 엔티티 생성 및 저장
		List<String> serviceNames = hospitalRequest.getServiceName();

		if (serviceNames != null && !serviceNames.isEmpty()) {
			List<ProvidedService> services = serviceNames.stream()
				.filter(name -> name != null && !name.trim().isEmpty())
				.map(name -> ProvidedService.create(name.trim(), hospital))
				.collect(Collectors.toList());

			hospitalProvider.saveServices(services); // 여러 개 저장
		}

		return hospital.getHospitalId();
	}

	/**
	 * 병원 정보 수정
	 *
	 * @return
	 */

	@Transactional
	@Override
	public Long updateHospital(Long hospitalId, HospitalUpdate dto) {
		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		// 병원 기본 정보 업데이트
		hospital.updateFromDTO(dto);

		// 기존 서비스 모두 삭제
		providedServiceRepo.deleteByHospitalHospitalId(hospitalId);

		// 새 서비스 등록
		if (dto.getServiceNames() != null && !dto.getServiceNames().isEmpty()) {
			List<ProvidedService> newServices = dto.getServiceNames().stream()
				.filter(name -> name != null && !name.trim().isEmpty())
				.map(name -> ProvidedService.create(name.trim(), hospital))
				.collect(Collectors.toList());

			providedServiceRepo.saveAll(newServices);

		}

		return hospitalId;
	}
	/**
	 * 병원 삭제
	 *
	 */
	@Override
	public void deleteHospital(Long hospitalId) {
		// 병원 조회
		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		// 1. 병원이 가진 모든 의사를 조회
		List<Doctor> doctors = doctorRepo.findByHospital(hospital);

		// 2. 각 의사의 스케줄 먼저 삭제
		for (Doctor doctor : doctors) {
			doctorScheduleRepo.deleteByDoctor(doctor);
		}

		// 3. 의사 삭제
		doctorRepo.deleteAll(doctors);

		// 4. 병원의 서비스 삭제
		providedServiceRepo.deleteByHospital(hospital);

		// 5. 병원의 스케줄 삭제
		hospitalScheduleRepo.deleteByHospitalHospitalId(hospitalId);

		// 6. 병원 삭제
		hospitalRepo.delete(hospital);
	}





	/**
	 * 병원 전체 리스트(어드민)
	 *
	 */
	@Override
	public Page<HospitalResponse> getHospitals(Pageable pageable) {
		Page<Hospital> hospitals = hospitalRepo.findAll(pageable);

		return hospitals.map(hospital -> {
			List<String> serviceNames = providedServiceRepo.findByHospitalHospitalId(hospital.getHospitalId())
				.stream()
				.map(ProvidedService::getServiceName)
				.collect(Collectors.toList());

			return HospitalResponse.builder()
				.hospitalId(hospital.getHospitalId())
				.userId(hospital.getUser() != null ? hospital.getUser().getUserId() : null)
				.name(hospital.getName())
				.address(hospital.getAddress())
				.notice(hospital.getNotice())
				.introduction(hospital.getIntroduction())
				.latitude(hospital.getLatitude())
				.longitude(hospital.getLongitude())
				.phone(hospital.getPhone())
				.waiting(hospital.getWaiting())
				.businessRegistrationNumber(hospital.getBusinessRegistrationNumber())
				.serviceNames(serviceNames)
				.build();
		});
	}


	/**
	 * 병원 영업시간 등록
	 *
	 */
	@Override
	public List<HospitalScheduleRequest> saveSchedules(Long hospitalId, List<HospitalScheduleRequest> schedules) {
		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		List<HospitalScheduleRequest> savedDto = new ArrayList<>();

		for (HospitalScheduleRequest scheduleDTO : schedules) {
			HospitalSchedule schedule = HospitalSchedule.create(
				hospital,
				null, // ID는 생성 시 null로 넘기고, JPA가 자동 생성
				DayOfWeek.valueOf(String.valueOf(scheduleDTO.getDayOfWeek())),
				scheduleDTO.getOpenTime(),
				scheduleDTO.getCloseTime(),
				scheduleDTO.getLunchStart(),
				scheduleDTO.getLunchEnd()
			);

			HospitalSchedule saved = hospitalProvider.saveHospitalSchedule(schedule);

			// 저장된 엔티티로부터 DTO 재생성
			savedDto.add(new HospitalScheduleRequest(saved));
		}

		return savedDto;
	}

	/**
	 * 병원 영업시간 조회
	 *
	 */

	@Override
	public List<HospitalScheduleResponse> getSchedules(Long hospitalId) {
		return hospitalScheduleRepo.findByHospitalHospitalId(hospitalId).stream()
			.map(schedule -> new HospitalScheduleResponse( //언더바없애기
				schedule.getHospitalScheduleId(),
				schedule.getDayOfWeek(),
				schedule.getOpenTime(),
				schedule.getCloseTime(),
				schedule.getLunchStart(),
				schedule.getLunchEnd()
			))
			.collect(Collectors.toList());
	}


	/**
	 * 병원 영업시간 수정
	 *
	 */
	@Override
	@Transactional
	public void updateHospitalSchedule(Long hospitalId, Long scheduleId, HospitalScheduleRequest scheduleDto) {

		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		HospitalSchedule schedule = hospitalProvider.getScheduleByIdOrThrow(scheduleId);

		hospitalProvider.validateScheduleBelongsToHospital(schedule, hospital);

		schedule.updateSchedule(scheduleDto);


	}

	/**
	 * 병원 영업시간 삭제
	 *
	 */
	@Override
	public void deleteHospitalSchedules(Long hospitalScheduleId) {

		hospitalScheduleRepo.deleteByHospitalScheduleId(hospitalScheduleId);
	}

	/**
	 * 병원 웨이팅 등록
	 *
	 */
	@Override
	public Long saveHospitalWaiting(Long hospitalId, HospitalWaiting hospitalWaiting) {

		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		hospital.updateWaiting(hospitalWaiting.getWaiting());

		hospitalProvider.saveHospital(hospital);


		return hospital.getHospitalId();
	}
	/**
	 * 병원 웨이팅 조회
	 *
	 */
	@Override

	public Long getHospitalWaiting(Long hospitalId) {

		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		return hospital.getWaiting();
	}

	/**
	 * 병원 웨이팅 수정
	 *
	 */
	@Override
	@Transactional
	public Long updateHospitalWaiting(Long hospitalId, HospitalWaiting hospitalWaiting) {

		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		hospital.updateWaiting(hospitalWaiting.getWaiting());

		return hospital.getHospitalId();
	}



	// 병원별 전체 리뷰 조회
	@Override
	@Transactional(readOnly = true)
	public Page<HospitalReviewResponse> getReviews(Long hospitalId, Pageable pageable) {

		return reviewProvider.getHospitalReviews(hospitalId, pageable)
			.map(HospitalReviewResponse::fromEntity);

	}

}


