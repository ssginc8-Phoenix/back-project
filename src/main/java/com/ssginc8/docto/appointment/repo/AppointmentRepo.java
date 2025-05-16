package com.ssginc8.docto.appointment.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.appointment.entity.Appointment;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

	@EntityGraph(attributePaths = {
		"hospital",
		"doctor.user",
		"patientGuardian.patient.user"
	})
	Optional<Appointment> findById(Long appointmentId);
}
