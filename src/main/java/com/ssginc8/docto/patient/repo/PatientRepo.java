package com.ssginc8.docto.patient.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.patient.entity.Patient;

public interface PatientRepo extends JpaRepository<Patient, Long> {
	Optional<Patient> findByPatientIdAndDeletedAtIsNull(Long patientId);

}
