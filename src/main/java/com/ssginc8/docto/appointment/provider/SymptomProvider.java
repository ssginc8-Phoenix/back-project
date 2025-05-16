package com.ssginc8.docto.appointment.provider;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.Symptom;
import com.ssginc8.docto.appointment.repo.SymptomRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SymptomProvider {

	private final SymptomRepo symptomRepo;

	@Transactional(readOnly = true)
	public List<String> getSymptomsByAppointment(Appointment appointment) {
		List<Symptom> symptoms = symptomRepo.findByAppointment(appointment);

		return symptoms.stream()
			.map(Symptom :: getName)
			.collect(Collectors.toList());
	}
}
