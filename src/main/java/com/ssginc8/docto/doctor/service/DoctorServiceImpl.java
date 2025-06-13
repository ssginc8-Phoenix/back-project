package com.ssginc8.docto.doctor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ssginc8.docto.doctor.dto.DoctorSaveRequest;
import com.ssginc8.docto.doctor.dto.DoctorResponse;
import com.ssginc8.docto.doctor.dto.DoctorScheduleList;
import com.ssginc8.docto.doctor.dto.DoctorScheduleRequest;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;
import com.ssginc8.docto.doctor.provider.DoctorProvider;
import com.ssginc8.docto.doctor.provider.DoctorScheduleProvider;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.provider.HospitalProvider;

import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

	private final DoctorProvider doctorProvider;
	private final DoctorScheduleProvider doctorScheduleProvider;
	private final HospitalProvider hospitalProvider;
	private final UserProvider userProvider;

	/**
	 * 의사 등록
	 *
	 */
	@Override
	public Long saveDoctor(DoctorSaveRequest doctorSaveRequest) {

		User user = userProvider.getUserById(doctorSaveRequest.getUserId());

		doctorProvider.validateUserIsDoctor(user);
		doctorProvider.validateUserIsNotAlreadyDoctor(user);

		Hospital hospital = hospitalProvider.getHospitalById(doctorSaveRequest.getHospitalId());

		Doctor doctor = Doctor.create(hospital, doctorSaveRequest.getSpecialization(),user);

		return doctorProvider.saveDoctor(doctor).getDoctorId();
	}


	/**
	 * 전체 의사 조회
	 *
	 */
	@Override
	public Page<DoctorResponse> getDoctors(Pageable pageable) {
		Page<Doctor> doctors = doctorProvider.getAllDoctors(pageable);

		return doctors.map(DoctorResponse::from);
	}

	/**
	 * 해당 병원 의사 조회
	 *
	 */
	@Override
	public List<DoctorResponse> getDoctorsByHospital(Long hospitalId) {
		hospitalProvider.getHospitalById(hospitalId);
		List<Doctor> doctors = doctorProvider.getDoctorsByHospitalId(hospitalId);

		return doctors.stream()
			.map(DoctorResponse::from)
			.collect(Collectors.toList());

	}
	/**
	 * 의사 영업시간 등록
	 *
	 */
	@Override
	public List<DoctorScheduleRequest> saveDoctorSchedule(Long doctorId, List<DoctorScheduleRequest> doctorScheduleRequest) {
		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		List<DoctorSchedule> savedSchedules = new ArrayList<>();

		for (DoctorScheduleRequest scheduleList : doctorScheduleRequest) {
			doctorScheduleProvider.validateWithinHospitalSchedule(doctor, scheduleList);
			DoctorSchedule schedule = DoctorSchedule.create(
				doctor,
				scheduleList.getDayOfWeek(),
				scheduleList.getStartTime(),
				scheduleList.getEndTime(),
				scheduleList.getLunchStart(),
				scheduleList.getLunchEnd()
			);

			DoctorSchedule saved = doctorScheduleProvider.saveDoctorSchedule(schedule);

			savedSchedules.add(saved);
		}

		// Entity → DTO 변환 후 반환
		return savedSchedules.stream()
			.map(DoctorScheduleRequest::new)
			.collect(Collectors.toList());
	}




	/**
	 * 의사 영업시간 조회
	 *
	 */
	@Override
	public List<DoctorScheduleList> getDoctorSchedule(Long doctorId) {
		doctorProvider.getDoctorById(doctorId);
		List<DoctorSchedule> schedules = doctorScheduleProvider.getSchedulesByDoctorId(doctorId);

		return schedules.stream()
			.map(DoctorScheduleList::new)
			.collect(Collectors.toList());
	}

	/**
	 * 의사 영업시간 수정
	 *
	 * @return
	 */
	@Override
	public DoctorScheduleRequest updateDoctorSchedule(Long doctorId, Long scheduleId, DoctorScheduleRequest doctorScheduleRequest) {
		Doctor doctor = doctorProvider.getDoctorById(doctorId);

		DoctorSchedule schedule = doctorScheduleProvider.getDoctorScheduleById(scheduleId);

		doctorProvider.validateScheduleBelongsToDoctor(schedule, doctor.getDoctorId());

		doctorScheduleProvider.validateWithinHospitalSchedule(doctor, doctorScheduleRequest);

		schedule.updateDoctorSchedule(
			doctorScheduleRequest.getDayOfWeek(),
			doctorScheduleRequest.getStartTime(),
			doctorScheduleRequest.getEndTime(),
			doctorScheduleRequest.getLunchStart(),
			doctorScheduleRequest.getLunchEnd());

		return doctorScheduleRequest;
	}

	/**
	 * 의사 영업시간 삭제
	 *
	 */
	@Override
	public void deleteDoctorSchedule(Long doctorId, Long scheduleId) {

		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		DoctorSchedule schedule = doctorScheduleProvider.getDoctorScheduleById(scheduleId);

		doctorProvider.validateScheduleBelongsToDoctor(schedule, doctor.getDoctorId());
    
		doctorScheduleProvider.deleteDoctorSchedule(schedule);
	}

	/**
	 * 의사가 30분 당 받을 수 있는 최대 환자 수 수정
	 */
	@Override
	public void updateCapacityPerHalfHour(Long doctorId, Long capacityPerHalfHour) {
		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		doctor.changeCapacityPerHalfHour(capacityPerHalfHour);
	}

	@Override
	public DoctorResponse getDoctorInfoByUserId(Long userId) {
		Doctor doctor = doctorProvider.getDoctorByUserId(userId);
		return DoctorResponse.from(doctor);
	}
}
