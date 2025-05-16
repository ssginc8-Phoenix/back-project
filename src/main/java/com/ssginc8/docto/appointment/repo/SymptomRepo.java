package com.ssginc8.docto.appointment.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.Symptom;

public interface SymptomRepo extends JpaRepository<Symptom, Long> {

	List<Symptom> findByAppointment(Appointment appointment);
}
