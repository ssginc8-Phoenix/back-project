package com.ssginc8.docto.patient.service;

import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.dto.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
	Long createPatient(PatientRequest dto);

	Page<PatientResponse> getAllPatients(Pageable pageable);

	void deletePatient(Long patientId);
}
