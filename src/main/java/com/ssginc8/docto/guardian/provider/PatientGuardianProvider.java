package com.ssginc8.docto.guardian.provider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.global.error.exception.guardianException.GuardianNotFoundException;
import com.ssginc8.docto.global.error.exception.guardianException.GuardianRequestNotFoundException;
import com.ssginc8.docto.global.error.exception.patientException.PatientNotFoundException;
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

	public List<PatientGuardian> getPatientGuardianListByGuardian(User user) {
		return patientGuardianRepo.findAllByUser(user);
	}

	@Transactional
	public void deleteMapping(Long guardianId, Long patientId) {
		patientGuardianRepo.softDeleteByUserIdAndPatientId(guardianId, patientId);
	}

	public PatientGuardian validateAndGetPatientGuardian(User guardian, Patient patient) {
		return patientGuardianRepo.findByUserAndPatient(guardian, patient)
			.filter(pg -> pg.getDeletedAt() == null) // 논리삭제 제외
			.filter(pg -> pg.getStatus() == Status.ACCEPTED) // 수락된 상태만
			.orElseThrow(GuardianMappingNotFoundException::new); // 보호자-환자 매핑 없으면 에러
	}

	@Transactional(readOnly = true)
	public List<PatientGuardian> getAllAcceptedGuardiansByPatientId(Long patientId) {
		// return patientGuardianRepo.findByPatient_PatientIdAndStatusAndDeletedAtIsNull(patientId, Status.ACCEPTED);
		return patientGuardianRepo
			.findByPatient_PatientIdAndStatus(patientId, Status.ACCEPTED)
			.stream()
			// deletedAt이 null인(=아직 활성화된) 매핑만 남긴다
			.filter(pg -> pg.getDeletedAt() == null)
			.collect(Collectors.toList());
	}

	public Optional<PatientGuardian> findByUserAndPatient(User guardian, Patient patient) {
		return patientGuardianRepo.findByUserAndPatient(guardian, patient);
	}

	@Transactional(readOnly = true)
	public Optional<PatientGuardian> findPendingOrAcceptedMapping(User guardian, Patient patient) {
		return patientGuardianRepo.findByUserAndPatient(guardian, patient)
			.filter(pg -> pg.getDeletedAt() == null) // 논리 삭제 안 된 것만
			.filter(pg -> pg.getStatus() == Status.PENDING || pg.getStatus() == Status.ACCEPTED); // 초대 대기 중 또는 수락된 보호자
	}

	public PatientGuardian findPendingMapping(User guardian, Patient patient) {
		return patientGuardianRepo.findByUserAndPatientAndStatus(guardian, patient, Status.PENDING)
			.orElse(null); // 없으면 null
	}

	/**
	 *
	 * @param patientGuardianId
	 * @return
	 */
	@Transactional(readOnly = true)
	public PatientGuardian getPatientGuardianById(Long patientGuardianId) {
		return patientGuardianRepo.findByIdWithPatientAndUser(patientGuardianId)
			.orElseThrow(GuardianNotFoundException::new);
	}

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
