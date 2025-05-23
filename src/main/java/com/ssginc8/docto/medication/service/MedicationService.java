package com.ssginc8.docto.medication.service;

import java.util.List;

import com.ssginc8.docto.medication.dto.MedicationCompleteRequest;
import com.ssginc8.docto.medication.dto.MedicationLogResponse;
import com.ssginc8.docto.medication.dto.MedicationScheduleRequest;
import com.ssginc8.docto.medication.dto.MedicationScheduleResponse;
import com.ssginc8.docto.medication.dto.MedicationUpdateRequest;

/**
 * MedicationService
 *
 * 복약 관리 기능의 비즈니스 로직을 정의하는 서비스 인터페이스
 * - 복약 시간 등록, 수정, 삭제
 * - 복약 완료 처리
 * - 복약 스케줄 및 기록 조회 등
 */
public interface MedicationService {

	/**
	 * 약 복용 스케줄 등록
	 *
	 * @param request 복약 스케줄 등록 요청 DTO
	 *                (약 이름, 복약 시간, 복용 요일, patientGuardianId 포함)
	 */
	void registerMedicationSchedule(MedicationScheduleRequest request);

	/**
	 * 특정 보호자(userId)가 가진 환자의 복약 기록 전체 조회
	 *
	 * @param userId 보호자 ID (실제는 patientGuardianId 역할)
	 * @return 복약 로그 응답 리스트
	 */
	List<MedicationLogResponse> getMedicationLogsByUser(Long userId);

	/**
	 * 특정 보호자(userId)가 가진 환자의 복약 스케줄 목록 조회
	 *
	 * @param userId 보호자 ID (실제는 patientGuardianId 역할)
	 * @return 약 이름, 복약 시간, 요일이 포함된 복약 스케줄 리스트
	 */
	List<MedicationScheduleResponse> getMedicationSchedulesByUser(Long userId);

	/**
	 * 복약 완료 처리
	 *
	 * @param medicationId 약 ID
	 * @param request 복약 완료 처리 요청 DTO (복약 상태 TAKEN 또는 MISSED 포함)
	 */
	void completeMedication(Long medicationId, MedicationCompleteRequest request);

	/**
	 * 약 복용 시간 변경
	 *
	 * @param medicationId 약 ID
	 * @param request 복약 시간 수정 요청 DTO (변경할 시간 포함)
	 */
	void updateMedicationTime(Long medicationId, MedicationUpdateRequest request);

	/**
	 * 약 복용 스케줄 삭제
	 *
	 * @param medicationId 삭제할 약 ID
	 */
	void deleteMedicationSchedule(Long medicationId);
}
