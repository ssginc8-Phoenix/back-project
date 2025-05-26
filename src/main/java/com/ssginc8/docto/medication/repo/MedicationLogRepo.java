package com.ssginc8.docto.medication.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.medication.entity.MedicationInformation;
import com.ssginc8.docto.medication.entity.MedicationLog;

/**
 * 복약 기록(MedicationLog)을 관리하는 Repo
 * - 약 기준으로 복약 로그 전체 조회
 */
public interface MedicationLogRepo extends JpaRepository<MedicationLog, Long> {

	/**
	 * 특정 약(medication)에 대한 복약 기록 전체 조회
	 */
	List<MedicationLog> findByMedication(MedicationInformation medication);
}