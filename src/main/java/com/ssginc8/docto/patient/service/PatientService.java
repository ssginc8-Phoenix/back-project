package com.ssginc8.docto.patient.service;

import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.dto.PatientResponse;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.provider.PatientProvider;
import com.ssginc8.docto.patient.repo.PatientRepo;
import com.ssginc8.docto.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 환자 관련 비즈니스 로직 처리
 */
@Service
@RequiredArgsConstructor
public class PatientService {

	private final PatientRepo patientRepo;
	private final PatientProvider patientProvider;

	@PersistenceContext
	private final EntityManager em;

	/**
	 * 환자 등록
	 */
	@Transactional
	public PatientResponse createPatient(PatientRequest dto) {
		User user = em.getReference(User.class, dto.getUserId());
		Patient patient = Patient.create(user, dto.getResidentRegistrationNumber());
		return PatientResponse.from(patientRepo.save(patient));
	}

	/**
	 * 모든 환자 목록 조회
	 */
	@Transactional
	public List<PatientResponse> getAllPatients() {
		return patientRepo.findAll().stream()
			.map(PatientResponse::from)
			.collect(Collectors.toList());
	}

	/**
	 * 환자 삭제 (소프트 삭제)
	 */
	@Transactional
	public void deletePatient(Long patientId) {
		Patient patient = patientProvider.getActivePatient(patientId);
		patient.softDelete();
	}
}