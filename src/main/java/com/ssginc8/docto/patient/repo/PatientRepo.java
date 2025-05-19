package com.ssginc8.docto.patient.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.patient.entity.Patient;

/**
 * 환자 엔티티 전용 JPA 레포지토리
 */
public interface PatientRepo extends JpaRepository<Patient, Long> {

	/**
	 * 삭제되지 않은 환자 정보만 조회
	 */
	Optional<Patient> findByPatientIdAndDeletedAtIsNull(Long patientId);
}