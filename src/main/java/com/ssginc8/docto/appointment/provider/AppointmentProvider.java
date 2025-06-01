package com.ssginc8.docto.appointment.provider;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;
import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.AppointmentStatus;
import com.ssginc8.docto.appointment.repo.AppointmentRepo;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.global.error.exception.appointmentException.AppointmentNotFoundException;
import com.ssginc8.docto.guardian.entity.PatientGuardian;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppointmentProvider {

	private final AppointmentRepo appointmentRepo;

	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentListByCondition(Pageable pageable, AppointmentSearchCondition condition) {

		return appointmentRepo.findAllByCondition(condition, pageable);
	}

	@Transactional(readOnly = true)
	public Appointment getAppointmentById(Long appointmentId) {
		return appointmentRepo.findById(appointmentId)
			.orElseThrow(AppointmentNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public boolean existsDuplicateAppointment(
		PatientGuardian patientGuardian, Doctor doctor, LocalDateTime appointmentTime
	) {
		return appointmentRepo.existsByPatientGuardianAndDoctorAndAppointmentTimeAndStatusNot(
			patientGuardian, doctor, appointmentTime, AppointmentStatus.CANCELED);
	}

	@Transactional
	public Appointment save(Appointment appointment) {
		return appointmentRepo.save(appointment);
	}

	@Transactional(readOnly = true)
	public boolean existsByPatientAndTimeRange(Long patientId, LocalDateTime start, LocalDateTime end) {
		return appointmentRepo.existsByPatientGuardian_Patient_PatientIdAndAppointmentTimeBetween(patientId, start, end);
	}

	@Transactional(readOnly = true)
	public int countAppointmentsInSlot(Long doctorId, LocalDateTime slotStart, LocalDateTime slotEnd) {
		return appointmentRepo.countAppointmentsInSlot(doctorId, slotStart, slotEnd);
	}

	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentsByPatient(Long userId, Pageable pageable) {
		return appointmentRepo.findByPatientGuardian_Patient_User_UserId(userId, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentsByGuardian(Long userId, Pageable pageable) {
		return appointmentRepo.findByPatientGuardian_User_UserId(userId, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentsByDoctor(Long userId, Pageable pageable) {
		return appointmentRepo.findByDoctor_User_UserId(userId, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentsByHospital(Long userId, Pageable pageable) {
		return appointmentRepo.findByHospital_User_UserId(userId, pageable);
	}
}
