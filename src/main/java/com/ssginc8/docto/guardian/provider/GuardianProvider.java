package com.ssginc8.docto.guardian.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.global.error.exception.guardianException.GuardianMappingNotFoundException;
import com.ssginc8.docto.global.error.exception.guardianException.GuardianRequestNotFoundException;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.repo.PatientGuardianRepo;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuardianProvider {

	private final PatientGuardianRepo patientGuardianRepo;

	public PatientGuardian getById(Long requestId) {
		return patientGuardianRepo.findById(requestId)
			.filter(pg -> pg.getDeletedAt() == null)
			.orElseThrow(GuardianRequestNotFoundException::new);
	}

	public PatientGuardian getMapping(Long guardianId, Long patientId) {
		return patientGuardianRepo.findByUserIdAndPatientId(guardianId, patientId)
			.filter(pg -> pg.getDeletedAt() == null)
			.orElseThrow(GuardianMappingNotFoundException::new);
	}

	public List<PatientGuardian> getAllAcceptedMappings(Long guardianId) {
		return patientGuardianRepo.findAcceptedPatientsByUserId(guardianId);
	}

	public PatientGuardian findByInviteCode(String inviteCode) {
		return patientGuardianRepo.findByInviteCode(inviteCode)
			.orElseThrow(() -> new GuardianRequestNotFoundException());
	}

}
