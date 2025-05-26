package com.ssginc8.docto.medication.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.medication.entity.MedicationAlertTime;
import com.ssginc8.docto.medication.entity.MedicationInformation;

/**
 * 복약 시간(MedicationAlertTime)을 관리하는 Repo
 * - 특정 약(medication)에 해당하는 복약 시간 리스트 조회
 */
public interface MedicationAlertTimeRepo extends JpaRepository<MedicationAlertTime, Long> {

	/**
	 * 약(medication)에 해당하는 복약 시간 전체 조회
	 */
	List<MedicationAlertTime> findByMedication(MedicationInformation medication);

	/**
	 * 복약 시간 ID와 약 ID 기준으로 단건 조회
	 */
	Optional<MedicationAlertTime> findByMedication_MedicationIdAndMedicationAlertTimeId(Long medicationId, Long alertTimeId);
}