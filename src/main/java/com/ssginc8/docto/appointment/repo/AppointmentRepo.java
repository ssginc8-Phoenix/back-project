package com.ssginc8.docto.appointment.repo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.guardian.entity.PatientGuardian;

public interface AppointmentRepo extends JpaRepository<Appointment, Long>, QuerydslPredicateExecutor<Appointment> {

	@EntityGraph(attributePaths = {
		"hospital",
		"doctor.user",
		"patientGuardian.patient.user"
	})
	Optional<Appointment> findById(Long appointmentId);

	boolean existsByPatientGuardianAndDoctorAndAppointmentTime(PatientGuardian patientGuardian, Doctor doctor, LocalDateTime appointmentTime);
}
