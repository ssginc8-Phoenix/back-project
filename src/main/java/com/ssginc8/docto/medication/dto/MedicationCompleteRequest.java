package com.ssginc8.docto.medication.dto;

import com.ssginc8.docto.medication.entity.MedicationStatus;

import lombok.Getter;

/**
 * 복약 완료 처리 요청 DTO
 *
 * - 복용 상태를 클라이언트에서 전달 (TAKEN, MISSED)
 */
@Getter
public class MedicationCompleteRequest {

	private MedicationStatus status; // 복용 상태
}