package com.ssginc8.docto.patient.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.patient.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	Optional<Patient> findByPatientIdAndDeletedAtIsNull(Long patientId);

}
