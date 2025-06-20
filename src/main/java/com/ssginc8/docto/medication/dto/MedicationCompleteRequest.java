package com.ssginc8.docto.medication.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 복약 완료 처리 요청 DTO
 *
 * - 복용 상태를 클라이언트에서 전달 (TAKEN, MISSED)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationCompleteRequest {
	/**
	 * 복용 완료할 특정 MedicationAlertTime의 고유 ID
	 * 이 ID를 통해 해당 복약 스케쥴의 특정 시간을 식별함
	 */
	private Long medicationAlertTimeId;

	/**
	 * 사용자가 실제로 약을 복용 완료한 시각
	 * 이 값은 MedicationLog 엔티티의 loggedAt 필드에 저장됨
	 */
	private OffsetDateTime completedAt;
}
