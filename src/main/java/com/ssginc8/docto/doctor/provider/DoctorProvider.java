package com.ssginc8.docto.doctor.provider;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;
import com.ssginc8.docto.doctor.repo.DoctorRepo;
import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;
import com.ssginc8.docto.global.error.exception.doctorException.DoctorAlreadyExistsException;
import com.ssginc8.docto.global.error.exception.doctorException.DoctorNotFoundException;
import com.ssginc8.docto.global.error.exception.doctorException.NotDoctorRoleException;
import com.ssginc8.docto.global.error.exception.doctorException.ScheduleNotInDoctorException;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.repo.HospitalScheduleRepo;
import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DoctorProvider {

	private final DoctorRepo doctorRepo;

	@Value("${cloud.default.image.address}")
	private String defaultProfileUrl;

	public Doctor getDoctorByUserId(Long userId) {
		return doctorRepo.findByUserUserId(userId)
			.orElseThrow(DoctorNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public Doctor getDoctorById(Long doctorId) {
		return doctorRepo.findById(doctorId)
			.orElseThrow(DoctorNotFoundException::new);
	}

	public Doctor saveDoctor(Doctor doctor) {
		doctorRepo.save(doctor);
		return doctor;
	}

	public void validateUserIsNotAlreadyDoctor(User user) {
		if (doctorRepo.existsByUser(user)) {
			throw new DoctorAlreadyExistsException(ErrorCode.DOCTOR_ALREADY_EXISTS);
		}
	}


	public List<Doctor> findByHospital(Hospital hospital) {
		return doctorRepo.findByHospital(hospital); // 이 부분이 null 방지 필요
	}

	public Page<Doctor> getAllDoctors(Pageable pageable) {
		return doctorRepo.findByDeletedAtIsNull(pageable);
	}

	public List<Doctor> getDoctorsByHospitalId(Long hospitalId) {
		return doctorRepo.findByHospitalHospitalId(hospitalId);
	}


	public void validateScheduleBelongsToDoctor(DoctorSchedule schedule, Long doctorId) {
		if (!schedule.getDoctor().getDoctorId().equals(doctorId)) {
			throw new ScheduleNotInDoctorException();
		}
	}

	public void validateUserIsDoctor(User user) {
		if (user.getRole() != Role.DOCTOR) {
			throw new NotDoctorRoleException();
		}
	}

	public String getImageUrlOrNull(Doctor doctor) {
		if (doctor.getUser() != null && doctor.getUser().getProfileImage() != null) {
			return doctor.getUser().getProfileImage().getUrl();
		}
		return defaultProfileUrl;
	}
}
