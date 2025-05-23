package com.ssginc8.docto.patient.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.repo.PatientRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PatientProvider {

	private final PatientRepo patientRepo;

	@Transactional(readOnly = true)
	public Patient getPatientById(Long patientId) {
		return patientRepo.findById(patientId).orElseThrow(
			() -> new IllegalArgumentException("해당 환자가 존재하지 않습니다. id = " + patientId)
		);
	}
}
