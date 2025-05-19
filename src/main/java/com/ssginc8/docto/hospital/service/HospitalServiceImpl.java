package com.ssginc8.docto.hospital.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.hospital.dto.HospitalListDTO;
import com.ssginc8.docto.hospital.dto.HospitalNameDTO;
import com.ssginc8.docto.hospital.dto.HospitalRequestDTO;
import com.ssginc8.docto.hospital.dto.HospitalScheduleDTO;
import com.ssginc8.docto.hospital.dto.HospitalWaitingDTO;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.entity.HospitalSchedule;
import com.ssginc8.docto.hospital.repository.HospitalRepository;
import com.ssginc8.docto.hospital.repository.HospitalScheduleRepository;
import com.ssginc8.docto.hospital.repository.UserRepository;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {


	private final UserRepository userRepository;
	private final HospitalRepository hospitalRepository;

	private final HospitalScheduleRepository scheduleRepository;
	private final HospitalScheduleRepository hospitalScheduleRepository;

	public List<HospitalListDTO> getHospitalId(Long hospitalId) {
		Optional<Hospital> hospitals = hospitalRepository.findById(hospitalId);

		return hospitals.stream()
			.map(h -> new HospitalListDTO(
				h.getHospitalId(),
				h.getUser().getUserId(),
				h.getName(),
				h.getAddress(),
				h.getNotice(),
				h.getPhone(),
				h.getIntroduction(),
				h.getWaiting(),
				h.getLongitude(),
				h.getLatitude(),
				h.getBusinessRegistrationNumber()




			))
			.collect(Collectors.toList());
	}

	@Override
	public List<HospitalListDTO> getHospitalsWithinRadius(double lat, double lng, double radius) {


		return hospitalRepository.findHospitalsWithinRadius(lat, lng, radius)
			.stream()
			.map(HospitalListDTO::new)
			.collect(Collectors.toList());
	}

	@Override //병원 정보 수정
	public HospitalRequestDTO updateHospital(Long hospitalId, HospitalRequestDTO hospitalRequestDTO) {
		Hospital hospital = hospitalRepository.findById(hospitalId)
			.orElseThrow(() -> new EntityNotFoundException("Hospital not found with id: " + hospitalId));


		hospital.setName(hospitalRequestDTO.getName());
		hospital.setAddress(hospitalRequestDTO.getAddress());
		hospital.setPhone(hospitalRequestDTO.getPhone());
		hospital.setIntroduction(hospitalRequestDTO.getIntroduction());
		hospital.setBusinessRegistrationNumber(hospitalRequestDTO.getBusinessRegistrationNumber());
		hospital.setNotice(hospitalRequestDTO.getNotice());


		Hospital updatedHospital = hospitalRepository.save(hospital);

		return new HospitalRequestDTO(updatedHospital);
	}

	@Override
	public void deleteHospital(Long hospitalId) {
		hospitalRepository.deleteById(hospitalId);
	}

	@Override//어드민용 병원 이름,주소
	public List<HospitalNameDTO> getHospitals() {
		return hospitalRepository.findAll().stream()
			.map(hospital -> new HospitalNameDTO(hospital.getHospitalId(),hospital.getName(),hospital.getAddress()))
			.collect(Collectors.toList());
	}

	@Override
	public List<HospitalScheduleDTO> saveSchedules(Long hospitalId, List<HospitalScheduleDTO> schedules) {
		Hospital hospital = hospitalRepository.findById(hospitalId)
			.orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다."));

		for (HospitalScheduleDTO scheduleDTO : schedules) {
			HospitalSchedule schedule = HospitalSchedule.create(
				hospital,
				DayOfWeek.valueOf(scheduleDTO.getDayOfWeek().toUpperCase()),
				scheduleDTO.getOpenTime(),
				scheduleDTO.getCloseTime(),
				scheduleDTO.getLunchStart(),
				scheduleDTO.getLunchEnd()
			);

			scheduleRepository.save(schedule);
		}
		return schedules;
	}


	@Override
	public List<HospitalScheduleDTO> getSchedules(Long hospitalId) {
		return hospitalScheduleRepository.findByHospital_HospitalId(hospitalId).stream()
			.map(hospital -> new HospitalScheduleDTO(
				hospital.getHospitalScheduleId(),
				hospital.getDayOfWeek(),
				hospital.getOpenTime(),
				hospital.getCloseTime(),
				hospital.getLunchStart(),
				hospital.getLunchEnd()
			))
			.collect(Collectors.toList());
	}


	@Override
	public void updateHospitalSchedules(Long hospitalId, List<HospitalScheduleDTO> schedules) {
		Hospital hospital = hospitalRepository.findById(hospitalId)
			.orElseThrow(() -> new EntityNotFoundException("Hospital not found with id: " + hospitalId));

		// 기존 스케줄 삭제
		scheduleRepository.deleteByHospitalId(hospitalId);

		// 새 스케줄 저장
		for (HospitalScheduleDTO dto : schedules) {
			HospitalSchedule schedule = HospitalSchedule.create(
				hospital,
				DayOfWeek.valueOf(dto.getDayOfWeek().toUpperCase()),
				dto.getOpenTime(),
				dto.getCloseTime(),
				dto.getLunchStart(),
				dto.getLunchEnd()
			);

			scheduleRepository.save(schedule);
		}
	}

	@Override
	public void deleteHospitalSchedules(Long hospitalId) {
		scheduleRepository.deleteByHospitalId(hospitalId);
	}

	@Override
	public Long saveHospitalWaiting(Long hospitalId, HospitalWaitingDTO hospitalWaitingDTO) {

		Hospital hospital = hospitalRepository.findById(hospitalId)
			.orElseThrow(() -> new EntityNotFoundException("Hospital not found with id: " + hospitalId));

		hospital.setWaiting(hospitalWaitingDTO.getWaiting());

		hospitalRepository.save(hospital);

		return hospital.getHospitalId();
	}


	@Override // 병원 정보 등록
	public Long saveHospital(HospitalListDTO hospitalListDTO) {

		User user = userRepository.findById(hospitalListDTO.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		// 정적 팩토리 메서드로 병원 생성
		Hospital hospital = Hospital.create(
			user,
			hospitalListDTO.getName(),
			hospitalListDTO.getAddress(),
			null, // 위도
			null, // 경도
			hospitalListDTO.getPhone(),
			hospitalListDTO.getIntroduction(),
			hospitalListDTO.getNotice(),
			hospitalListDTO.getWaiting(),
			hospitalListDTO.getBusinessRegistrationNumber()
		);

		hospitalRepository.save(hospital);
		return hospital.getHospitalId();
	}


	// @Override //병원 정보 등록(병원+스케쥴)
	// public Long saveHospitalWithSchedules(HospitalListDTO hospitalListDTO) {
	//
	// 	User user = userRepository.findById(hospitalListDTO.getUserId())
	// 		.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
	// 	// 병원 정보 저장
	// 	Hospital hospital = Hospital.builder()
	// 		.user(user)
	// 		.name(hospitalListDTO.getName())
	// 		.address(hospitalListDTO.getAddress())
	// 		.phone(hospitalListDTO.getPhone())
	// 		.introduction(hospitalListDTO.getIntroduction())
	// 		.notice(hospitalListDTO.getNotice())
	// 		.businessRegistrationNumber(hospitalListDTO.getBusinessRegistrationNumber())
	// 		.waiting(hospitalListDTO.getWaiting())
	// 		.build();
	//
	// 	hospitalRepository.save(hospital);
	//
	// 	// 병원 스케줄 저장
	// 	for (HospitalScheduleDTO scheduleDTO : hospitalListDTO.getSchedules()) {
	// 		HospitalSchedule schedule = HospitalSchedule.builder()
	// 			.hospital(hospital)
	// 			.dayOfWeek(DayOfWeek.valueOf(scheduleDTO.getDayOfWeek().toUpperCase()))
	// 			.openTime(scheduleDTO.getOpenTime())
	// 			.closeTime(scheduleDTO.getCloseTime())
	// 			.lunchStart(scheduleDTO.getLunchStart())
	// 			.lunchEnd(scheduleDTO.getLunchEnd())
	// 			.build();
	//
	// 		scheduleRepository.save(schedule);
	// 	}
	//
	// 	// 병원 ID를 포함한 성공 응답 반환
	// 	return hospital.getHospitalId();
	// }



}


