package com.ssginc8.docto.appointment.provider;

import static com.ssginc8.docto.appointment.entity.QAppointment.*;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;
import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.repo.AppointmentRepo;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.guardian.entity.PatientGuardian;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppointmentProvider {

	private final AppointmentRepo appointmentRepo;

	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentListByCondition(Pageable pageable, AppointmentSearchCondition condition) {

		BooleanBuilder builder = new BooleanBuilder();

		if (condition.getUserId() != null) {
			builder.and(appointment.patientGuardian.patient.user.userId.eq(condition.getUserId()));
		}

		if (condition.getHospitalId() != null) {
			builder.and(appointment.hospital.hospitalId.eq(condition.getHospitalId()));
		}

		if (condition.getDoctorId() != null) {
			builder.and(appointment.doctor.user.userId.eq(condition.getDoctorId()));
		}

		return appointmentRepo.findAll(builder, pageable);
	}


	@Transactional(readOnly = true)
	public Appointment getAppointmentById(Long appointmentId) {
		return appointmentRepo.findById(appointmentId).orElseThrow(
			() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다. id = " + appointmentId));
	}

	@Transactional(readOnly = true)
	public boolean existsDuplicateAppointment(
		PatientGuardian patientGuardian, Doctor doctor, LocalDateTime appointmentTime
	) {
		return appointmentRepo.existsByPatientGuardianAndDoctorAndAppointmentTime(
			patientGuardian, doctor, appointmentTime);
	}

	@Transactional
	public Appointment save(Appointment appointment) {
		return appointmentRepo.save(appointment);
	}


}
