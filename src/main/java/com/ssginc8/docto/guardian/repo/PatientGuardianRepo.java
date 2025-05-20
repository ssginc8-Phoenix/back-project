package com.ssginc8.docto.guardian.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.entity.Status;

/**
 * 환자-보호자 관계를 관리하는 JPA 레포지토리
 */
public interface PatientGuardianRepo extends JpaRepository<PatientGuardian, Long> {

	Optional<PatientGuardian> findByPatientGuardianIdAndStatus(Long patientGuardianId, Status status);

	void deleteByUser_UserIdAndPatient_PatientId(Long userId, Long patientId);

	List<PatientGuardian> findAllByStatus(Status status);

	@Query("SELECT pg FROM PatientGuardian pg WHERE pg.user.userId = :userId AND pg.status = 'ACCEPTED'")
	List<PatientGuardian> findAcceptedPatientsByUserId(Long userId);
}