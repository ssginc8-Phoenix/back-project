package com.ssginc8.docto.patient.service;

import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.dto.PatientResponse;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.repo.PatientRepo;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

	private final PatientRepo patientRepository;

	@PersistenceContext
	private EntityManager em; // UserRepository 안 쓰고 EntityManager 사용

	@Transactional
	public PatientResponse createPatient(PatientRequest dto) {
		// 영속 상태 프록시 객체 생성
		User user = em.getReference(User.class, dto.getUserId());

		Patient patient = Patient.builder()
			.user(user)
			.residentRegistrationNumber(dto.getResidentRegistrationNumber())
			.build();

		Patient saved = patientRepository.save(patient);

		return new PatientResponse(
			saved.getPatientId(),
			saved.getUser().getUserId(),
			saved.getResidentRegistrationNumber()
		);
	}

	@Transactional
	public List<PatientResponse> getAllPatients() {
		return patientRepository.findAll().stream()
			.map(patient -> new PatientResponse(
				patient.getPatientId(),
				patient.getUser().getUserId(),
				patient.getResidentRegistrationNumber()
			))
			.collect(Collectors.toList());
	}

	@Transactional
	public void deletePatient(Long patientId) {
		Patient patient = patientRepository.findById(patientId)
			.orElseThrow(() -> new EntityNotFoundException("Patient not found"));

		patientRepository.delete(patient);
	}
}