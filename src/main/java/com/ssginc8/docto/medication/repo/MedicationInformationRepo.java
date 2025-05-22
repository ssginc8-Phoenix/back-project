package com.ssginc8.docto.medication.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.medication.entity.MedicationInformation;

/**
 * 복약 정보(MedicationInformation)를 관리하는 Repo
 * - 보호자-환자 관계 ID 기준으로 복약 정보를 조회
 */
public interface MedicationInformationRepo extends JpaRepository<MedicationInformation, Long> {

	// guardian 패키지 완성 시 아래 메서드를 사용하도록 수정 예정
    /*
    List<MedicationInformation> findByPatientGuardian(PatientGuardian patientGuardian);
    */

	/**
	 * guardian 패키지가 미완성이라 ID로 조회
	 */
	List<MedicationInformation> findByPatientGuardianId(Long patientGuardianId);
}
