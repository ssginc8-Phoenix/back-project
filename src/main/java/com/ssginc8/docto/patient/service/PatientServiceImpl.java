package com.ssginc8.docto.patient.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

/**
 * 환자 서비스 구현체
 * - PatientService 인터페이스 구현
 * - 환자 등록, 전체 조회, 삭제 처리 등의 비즈니스 로직 수행
 */
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

	private final PatientRepo patientRepo;
	private final PatientProvider patientProvider;

	@PersistenceContext
	private final EntityManager em;

	/**
	 * 환자 등록
	 * - User 엔티티는 getReference로 프록시 조회
	 * - 정적 팩토리 메서드로 Patient 생성 후 저장
	 */
	@Override
	@Transactional
	public PatientResponse createPatient(PatientRequest dto) {
		User user = em.getReference(User.class, dto.getUserId());
		Patient patient = Patient.create(user, dto.getResidentRegistrationNumber());
		return PatientResponse.from(patientRepo.save(patient));
	}

	/**
	 * 모든 환자 목록 조회
	 * - Patient 엔티티를 PatientResponse DTO로 변환하여 반환
	 */
	@Override
	@Transactional
	public List<PatientResponse> getAllPatients() {
		return patientRepo.findAll().stream()
			.map(PatientResponse::from)
			.collect(Collectors.toList());
	}

	/**
	 * 환자 삭제 처리 (soft delete)
	 * - provider를 통해 유효한 환자 엔티티 조회 후 삭제 처리
	 */
	@Override
	@Transactional
	public void deletePatient(Long patientId) {
		Patient patient = patientProvider.getActivePatient(patientId);
		patient.delete();
	}
}