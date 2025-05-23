package com.ssginc8.docto.guardian.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.user.entity.User;

public interface PatientGuardianRepo extends JpaRepository<PatientGuardian, Long> {

	Optional<PatientGuardian> findByUserAndPatient(User user, Patient patient);
}
