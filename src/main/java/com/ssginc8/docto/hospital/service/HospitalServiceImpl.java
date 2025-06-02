package com.ssginc8.docto.hospital.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.doctor.entity.Doctor;

import com.ssginc8.docto.doctor.provider.DoctorProvider;
import com.ssginc8.docto.doctor.provider.DoctorScheduleProvider;

import com.ssginc8.docto.hospital.dto.HospitalRequest;
import com.ssginc8.docto.hospital.dto.HospitalResponse;
import com.ssginc8.docto.hospital.dto.HospitalReviewResponse;
import com.ssginc8.docto.hospital.dto.HospitalScheduleRequest;
import com.ssginc8.docto.hospital.dto.HospitalScheduleResponse;
import com.ssginc8.docto.hospital.dto.HospitalUpdate;
import com.ssginc8.docto.hospital.dto.HospitalWaitingResponse;
import com.ssginc8.docto.hospital.dto.HospitalWaitingRequest;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.entity.HospitalSchedule;
import com.ssginc8.docto.hospital.entity.ProvidedService;
import com.ssginc8.docto.hospital.provider.HospitalProvider;
import com.ssginc8.docto.review.provider.ReviewProvider;
import com.ssginc8.docto.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

	private final HospitalProvider hospitalProvider;
	private final ReviewProvider reviewProvider;
	private final DoctorProvider doctorProvider;
	private final DoctorScheduleProvider doctorScheduleProvider;

	/**
	 *  위치기반 병원 리스트 조회
	 */
	@Override
	public Page<HospitalResponse> getHospitalsWithinRadius(double lat, double lng, double radius, Pageable pageable) {
		Page<Hospital> hospitals = hospitalProvider.findHospitalsWithinRadius(lat, lng, radius, pageable);
		return hospitals.map(HospitalResponse::new);
	}


	/**
	 * 병원아이디 상세 조회
	 *
	 */
	@Override
	public HospitalResponse getHospitalId(Long hospitalId) {

		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		// 병원 ID로 서비스 목록 조회 (반환 타입 List<ProvidedService>로 수정)
		List<ProvidedService> services = hospitalProvider.findServicesByHospitalId(hospitalId);

		// 서비스 이름들을 콤마로 연결
		String serviceNames = (services == null || services.isEmpty())
			? null
			: services.stream()
			.map(ProvidedService::getServiceName)
			.collect(Collectors.joining(", "));

		return HospitalResponse.builder()
			.hospitalId(hospital.getHospitalId())
			.name(hospital.getName())
			.address(hospital.getAddress())
			.latitude(hospital.getLatitude())
			.longitude(hospital.getLongitude())
			.phone(hospital.getPhone())
			.introduction(hospital.getIntroduction())
			.notice(hospital.getNotice())
			.waiting(hospital.getWaiting())
			// serviceNames가 null이면 빈 리스트로 대체
			.serviceNames(serviceNames == null ? Collections.emptyList() : Collections.singletonList(serviceNames))
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
			hospitalRequest.getBusinessRegistrationNumber(),
			hospitalRequest.getLongitude(),
			hospitalRequest.getLatitude(),
			hospitalRequest.getNotice()
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
		hospital.updateFromValues(
			dto.getName(),
			dto.getPhone(),
			dto.getAddress(),
			dto.getIntroduction(),
			dto.getNotice()
		);

		// 기존 서비스 모두 삭제
		hospitalProvider.deleteByHospitalHospitalId(hospitalId);

		// 새 서비스 등록
		if (dto.getServiceNames() != null && !dto.getServiceNames().isEmpty()) {
			List<ProvidedService> newServices = dto.getServiceNames().stream()
				.filter(name -> name != null && !name.trim().isEmpty())
				.map(name -> ProvidedService.create(name.trim(), hospital))
				.collect(Collectors.toList());

			hospitalProvider.saveServices(newServices);

		}

		return hospitalId;
	}
	/**
	 * 병원 삭제
	 *
	 */
	@Override
	@Transactional
	public void deleteHospital(Long hospitalId) {
		// 1. 병원 조회
		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		// 2. 병원이 가진 모든 의사 조회
		List<Doctor> doctors = doctorProvider.findByHospital(hospital);

		// 3. 각 의사의 스케줄 삭제
		for (Doctor doctor : doctors) {
			doctorScheduleProvider.deleteByDoctorId(doctor.getDoctorId());
		}

		// 4. 의사 삭제
		for (Doctor doctor : doctors) {
			doctor.delete();
		}

		// 5. 병원의 서비스 삭제
		hospitalProvider.deleteProvidedServicesByHospital(hospital);

		// 6. 병원 스케줄 삭제
		hospitalProvider.deleteByHospitalId(hospitalId);

		// 7. 병원 삭제
		hospital.softDelete();
	}






	/**
	 * 병원 전체 리스트(어드민)
	 *
	 */
	@Override
	public Page<HospitalResponse> getHospitals(Pageable pageable) {
		Page<Hospital> hospitals = hospitalProvider.findAll(pageable);

		return hospitals.map(hospital -> {
			List<String> serviceNames = hospitalProvider.findServicesByHospitalId(hospital.getHospitalId())
				.stream()
				.map(ProvidedService::getServiceName)
				.collect(Collectors.toList());

			return HospitalResponse.builder()
				.hospitalId(hospital.getHospitalId())
				.name(hospital.getName())
				.address(hospital.getAddress())
				.notice(hospital.getNotice())
				.introduction(hospital.getIntroduction())
				.latitude(hospital.getLatitude())
				.longitude(hospital.getLongitude())
				.phone(hospital.getPhone())
				.waiting(hospital.getWaiting())
				.serviceNames(serviceNames)
				.build();
		});
	}




	/**
	 * 병원 영업시간 등록
	 *
	 */
	@Override
	public void saveSchedules(Long hospitalId, List<HospitalScheduleRequest> schedules) {
		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		List<HospitalScheduleRequest> savedDto = new ArrayList<>();

		for (HospitalScheduleRequest scheduleDTO : schedules) {
			HospitalSchedule schedule = HospitalSchedule.create(
				hospital,
				scheduleDTO.getDayOfWeek(),
				scheduleDTO.getOpenTime(),
				scheduleDTO.getCloseTime(),
				scheduleDTO.getLunchStart(),
				scheduleDTO.getLunchEnd()
			);

			HospitalSchedule saved = hospitalProvider.saveHospitalSchedule(schedule);

			savedDto.add(new HospitalScheduleRequest(saved));
		}


	}



	/**
	 * 병원 영업시간 조회
	 */

@Override
public List<HospitalScheduleResponse> getSchedules(Long hospitalId) {

	hospitalProvider.getHospitalById(hospitalId); // 존재 여부 체크

	return hospitalProvider.findSchedulesByHospitalId(hospitalId).stream()
		.map(schedule -> new HospitalScheduleResponse(
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

		schedule.updateSchedule(
			scheduleDto.getDayOfWeek(),
			scheduleDto.getOpenTime(),
			scheduleDto.getCloseTime(),
			scheduleDto.getLunchStart(),
			scheduleDto.getLunchEnd()
		);


	}

	/**
	 * 병원 영업시간 삭제
	 *
	 */
	@Override
	public void deleteHospitalSchedules(Long hospitalScheduleId) {

		HospitalSchedule schedule = hospitalProvider.getScheduleByIdOrThrow(hospitalScheduleId); // 없으면 예외 던짐
		hospitalProvider.deleteByHospitalScheduleId(schedule.getHospitalScheduleId());
	}

	/**
	 * 병원 웨이팅 등록
	 *
	 */
	@Override
	public Long saveHospitalWaiting(Long hospitalId, HospitalWaitingRequest hospitalWaitingRequest) {

		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		hospital.updateWaiting(hospitalWaitingRequest.getWaiting());

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
	public void updateHospitalWaiting(Long hospitalId, HospitalWaitingRequest hospitalWaiting) {

		Hospital hospital = hospitalProvider.getHospitalById(hospitalId);

		hospital.updateWaiting(hospitalWaiting.getWaiting());


	}



	// 병원별 전체 리뷰 조회
	@Override
	@Transactional(readOnly = true)
	public Page<HospitalReviewResponse> getReviews(Long hospitalId, Pageable pageable) {

		hospitalProvider.getHospitalById(hospitalId);

		return reviewProvider.getHospitalReviews(hospitalId, pageable)
			.map(HospitalReviewResponse::fromEntity);

	}

}


