package com.ssginc8.docto.patient.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.repo.PatientRepo;

import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;

/**
 * Patient 조회 전용 계층
 * - 서비스 로직에서 중복되는 환자 조회/검증 처리 담당
 */

@Component
@RequiredArgsConstructor
public class PatientProvider {

	private final PatientRepo patientRepo;


	/**
	 * 삭제되지 않은 환자를 조회하거나 없으면 EntityNotFoundException 발생
	 */
	public Patient getActivePatient(Long id) {
		return patientRepo.findByPatientIdAndDeletedAtIsNull(id)
			.orElseThrow(() -> new EntityNotFoundException("Patient not found"));
	}
  
  @Transactional(readOnly = true)
	public Patient getPatientById(Long patientId) {
		return patientRepo.findById(patientId).orElseThrow(
			() -> new IllegalArgumentException("해당 환자가 존재하지 않습니다. id = " + patientId)
		);
	}
}


