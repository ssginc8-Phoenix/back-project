package com.ssginc8.docto.medication.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.medication.entity.MedicationAlertDay;
import com.ssginc8.docto.medication.entity.MedicationAlertTime;

/**
 * 복약 요일(MedicationAlertDay)을 관리하는 Repo
 * - 복약 시간 기준으로 요일 조회 및 삭제 처리
 */
public interface MedicationAlertDayRepo extends JpaRepository<MedicationAlertDay, Long> {

	/**
	 * 특정 복약 시간에 해당하는 요일 리스트 조회
	 */
	List<MedicationAlertDay> findByMedicationAlertTime(MedicationAlertTime medicationAlertTime);

	/**
	 * 복약 시간 기준으로 요일 전체 삭제
	 */
	void deleteByMedicationAlertTime(MedicationAlertTime medicationAlertTime);
}