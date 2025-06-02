package com.ssginc8.docto.guardian.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.entity.Status;
import com.ssginc8.docto.guardian.repo.PatientGuardianRepo;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.global.error.exception.guardianException.GuardianMappingNotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientGuardianProvider {

	private final PatientGuardianRepo patientGuardianRepo;

	@Transactional
	public PatientGuardian save(PatientGuardian patientGuardian) {
		return patientGuardianRepo.save(patientGuardian);
	}

	public PatientGuardian validateAndGetPatientGuardian(User guardian, Patient patient) {
		return patientGuardianRepo.findByUserAndPatient(guardian, patient)
			.filter(pg -> pg.getDeletedAt() == null) // 논리삭제 제외
			.filter(pg -> pg.getStatus() == Status.ACCEPTED) // 수락된 상태만
			.orElseThrow(GuardianMappingNotFoundException::new); // 보호자-환자 매핑 없으면 에러
	}
}
