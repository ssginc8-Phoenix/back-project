package com.ssginc8.docto.doctor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ssginc8.docto.doctor.dto.DoctorSaveRequest;
import com.ssginc8.docto.doctor.dto.DoctorUpdateRequest;
import com.ssginc8.docto.doctor.dto.DoctorResponse;
import com.ssginc8.docto.doctor.dto.DoctorScheduleList;
import com.ssginc8.docto.doctor.dto.DoctorScheduleRequest;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;
import com.ssginc8.docto.doctor.provider.DoctorProvider;
import com.ssginc8.docto.doctor.provider.DoctorScheduleProvider;
import com.ssginc8.docto.doctor.repo.DoctorRepo;
import com.ssginc8.docto.doctor.repo.DoctorScheduleRepo;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.repo.HospitalRepo;

import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.repo.UserRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

	private final DoctorProvider doctorProvider;
	private final UserRepo userRepo;
	private final DoctorRepo doctorRepo;
	private final HospitalRepo hospitalRepo;
	private final DoctorScheduleRepo doctorScheduleRepo;
	private final DoctorScheduleProvider doctorScheduleProvider;

	/**
	 * 의사 등록
	 *
	 */
	@Override
	public Long saveDoctor(DoctorSaveRequest doctorSaveRequest) {
		// 1. User 엔티티 생성 (팩토리 메서드 사용)
		User user = User.createUser(
			doctorSaveRequest.getUsername(),
			doctorSaveRequest.getPassword(),
			doctorSaveRequest.getEmail(),
			doctorSaveRequest.getLogin_type(),
			doctorSaveRequest.getRole(),
			doctorSaveRequest.isSuspended(),
			doctorSaveRequest.getUuid()
		);

		// 2. User 저장
		User savedUser = userRepo.save(user);

		// 3. Hospital 조회
		Hospital hospital = hospitalRepo.findById(doctorSaveRequest.getHospitalId())
			.orElseThrow(() -> new EntityNotFoundException("Hospital not found with id: " + doctorSaveRequest.getHospitalId()));

		// 4. Doctor 생성 (doctorId는 생성자에 넣지 않음)
		Doctor doctor = Doctor.create(hospital, savedUser, doctorSaveRequest.getSpecialization());

		// 5. Doctor 저장
		Doctor savedDoctor = doctorProvider.saveDoctor(doctor);

		return savedDoctor.getDoctorId();
	}

	/**
	 * 의사 정보 수정
	 *
	 */
	@Override
	public DoctorUpdateRequest updateDoctor(Long doctorId, DoctorUpdateRequest doctorUpdateRequest) {
		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		doctor.updateFromDTO(doctorUpdateRequest, userRepo); // null-safe 업데이트

		return new DoctorUpdateRequest(
			doctor.getUser().getPassword(),
			doctor.getUser().getEmail(),
			doctor.getSpecialization()
		);
	}

	/**
	 * 전체 의사 조회
	 *
	 */
	@Override
	public Page<DoctorResponse> getDoctors(Pageable pageable) {
		Page<Doctor> doctors = doctorRepo.findAll(pageable);

		return doctors.map(DoctorResponse::from);
	}

	/**
	 * 해당 병원 의사 조회
	 *
	 */
	@Override
	public List<DoctorResponse> getDoctorsByHospital(Long hospitalId) {

		List<Doctor> doctors = doctorRepo.findByHospitalHospitalId(hospitalId);

		return doctors.stream()
			.map(DoctorResponse::from)
			.collect(Collectors.toList());

	}

	/**
	 * 의사 영업시간 등록
	 *
	 */
	@Override
	public List<DoctorScheduleList> saveDoctorSchedule(Long doctorId, List<DoctorScheduleList> doctorScheduleList) {
		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		List<DoctorSchedule> savedSchedules = new ArrayList<>();

		for (DoctorScheduleList scheduleList : doctorScheduleList) {
			DoctorSchedule schedule = DoctorSchedule.create(
				doctor,
				scheduleList.getDayOfWeek(),
				scheduleList.getLunchStart(),
				scheduleList.getLunchEnd(),
				scheduleList.getStartTime(),
				scheduleList.getEndTime()
			);

			DoctorSchedule saved = doctorScheduleProvider.saveDoctorSchedule(schedule);

			savedSchedules.add(saved);
		}

		// Entity → DTO 변환 후 반환
		return savedSchedules.stream()
			.map(DoctorScheduleList::new)
			.collect(Collectors.toList());
	}

	/**
	 * 의사 영업시간 조회
	 *
	 */
	@Override
	public List<DoctorScheduleList> getDoctorSchedule(Long doctorId) {
		List<DoctorSchedule> schedules = doctorScheduleRepo.findAllByDoctorDoctorId(doctorId);

		return schedules.stream()
			.map(DoctorScheduleList::new)
			.collect(Collectors.toList());
	}

	/**
	 * 의사 영업시간 수정
	 *
	 */
	@Override
	public DoctorScheduleRequest updateDoctorSchedule(Long doctorId, Long scheduleId, DoctorScheduleRequest doctorScheduleRequest) {
		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		DoctorSchedule schedule = doctorScheduleProvider.getDoctorScheduleById(scheduleId);
		// 소속 검증
		if (!schedule.getDoctor().getDoctorId().equals(doctor.getDoctorId())) {
			throw new IllegalArgumentException("Schedule does not belong to the doctor");
		}

		schedule.updateDoctorSchedule(doctorScheduleRequest);

		return new DoctorScheduleRequest(
			schedule.getDayOfWeek(),
			schedule.getStartTime(),
			schedule.getEndTime(),
			schedule.getLunchStart(),
			schedule.getLunchEnd()
		);
	}

	/**
	 * 의사 영업시간 삭제
	 *
	 */
	@Override
	public void deleteDoctorSchedule(Long doctorId, Long scheduleId) {
		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		DoctorSchedule schedule = doctorScheduleProvider.getDoctorScheduleById(scheduleId);
		// 소속 검증
		if (!schedule.getDoctor().getDoctorId().equals(doctor.getDoctorId())) {
			throw new IllegalArgumentException("Schedule does not belong to the doctor");
		}

		doctorScheduleRepo.delete(schedule);


	}

}
