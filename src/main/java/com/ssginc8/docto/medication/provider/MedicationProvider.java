package com.ssginc8.docto.medication.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.global.error.exception.BusinessBaseException;
import com.ssginc8.docto.medication.entity.MedicationAlertTime;
import com.ssginc8.docto.medication.entity.MedicationInformation;
import com.ssginc8.docto.medication.repo.MedicationAlertTimeRepo;
import com.ssginc8.docto.medication.repo.MedicationInformationRepo;
import com.ssginc8.docto.global.error.ErrorCode;

// guardian 패키지 완성 시 사용
// import com.ssginc8.docto.guardian.entity.PatientGuardian;
// import com.ssginc8.docto.guardian.repo.PatientGuardianRepo;

import lombok.RequiredArgsConstructor;

/**
 * MedicationProvider
 *
 * 복약 정보 및 복약 시간 등의 엔티티 조회를 전담하는 Provider 계층
 * - Service 계층에서 호출
 * - guardian 패키지가 미완성이라 보호자 정보는 주석 처리
 */
@Component
@RequiredArgsConstructor
public class MedicationProvider {

	private final MedicationInformationRepo medicationInformationRepo;
	private final MedicationAlertTimeRepo medicationAlertTimeRepo;

	// guardian 패키지 완성 시 주석 해제
	// private final PatientGuardianRepo patientGuardianRepo;

	@Transactional(readOnly = true)
	public MedicationInformation getMedication(Long medicationId) {
		return medicationInformationRepo.findById(medicationId)
			.orElseThrow(() -> new BusinessBaseException(ErrorCode.INVALID_INPUT_VALUE));
	}

	@Transactional(readOnly = true)
	public MedicationAlertTime getMedicationAlertTime(Long alertTimeId) {
		return medicationAlertTimeRepo.findById(alertTimeId)
			.orElseThrow(() -> new BusinessBaseException(ErrorCode.INVALID_INPUT_VALUE));
	}

	@Transactional(readOnly = true)
	public MedicationAlertTime getAlertTimeByMedication(Long medicationId, Long alertTimeId) {
		return medicationAlertTimeRepo.findByMedication_MedicationIdAndMedicationAlertTimeId(medicationId, alertTimeId)
			.orElseThrow(() -> new BusinessBaseException(ErrorCode.INVALID_INPUT_VALUE));
	}

	// guardian 패키지 완성 시 주석 해제
    /*
    @Transactional(readOnly = true)
    public PatientGuardian getPatientGuardian(Long patientGuardianId) {
        return patientGuardianRepo.findById(patientGuardianId)
            .orElseThrow(() -> new BusinessBaseException(ErrorCode.PATIENT_GUARDIAN_NOT_FOUND));
    }
    */
}
