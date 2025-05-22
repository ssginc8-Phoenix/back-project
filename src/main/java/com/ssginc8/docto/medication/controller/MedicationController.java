package com.ssginc8.docto.medication.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssginc8.docto.medication.dto.*;
import com.ssginc8.docto.medication.service.MedicationService;

import lombok.RequiredArgsConstructor;

/**
 * 복약 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/medications")
@RequiredArgsConstructor
public class MedicationController {

	private final MedicationService medicationService;

	/**
	 * 복용 기록 조회
	 * - 특정 사용자(userId)의 전체 복약 로그 반환
	 */
	@GetMapping("/patients/{userId}")
	public ResponseEntity<List<MedicationLogResponse>> getLogsByUser(@PathVariable Long userId) {
		List<MedicationLogResponse> response = medicationService.getMedicationLogsByUser(userId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 복용 스케줄 조회
	 * - 특정 사용자(userId)의 복약 스케줄(약 + 요일 + 시간)
	 */
	@GetMapping("/schedules/{userId}")
	public ResponseEntity<List<MedicationScheduleResponse>> getSchedules(@PathVariable Long userId) {
		List<MedicationScheduleResponse> response = medicationService.getMedicationSchedulesByUser(userId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 약 복용 시간 등록
	 * - 약 이름 + 복약 시간 + 요일 정보 등록
	 */
	@PostMapping
	public ResponseEntity<Void> registerSchedule(@RequestBody MedicationScheduleRequest request) {
		medicationService.registerMedicationSchedule(request);
		return ResponseEntity.ok().build();
	}

	/**
	 * 복용 완료 처리
	 * - 복용 상태(TAKEN, MISSED)를 기록
	 */
	@PatchMapping("/{medicationId}/complete")
	public ResponseEntity<Void> completeMedication(@PathVariable Long medicationId,
		@RequestBody MedicationCompleteRequest request) {
		medicationService.completeMedication(medicationId, request);
		return ResponseEntity.ok().build();
	}

	/**
	 * 복용 시간 변경
	 */
	@PatchMapping("/{medicationId}")
	public ResponseEntity<Void> updateTime(@PathVariable Long medicationId,
		@RequestBody MedicationUpdateRequest request) {
		medicationService.updateMedicationTime(medicationId, request);
		return ResponseEntity.ok().build();
	}

	/**
	 * 약 복용 시간 삭제
	 */
	@DeleteMapping("/{medicationId}")
	public ResponseEntity<Void> deleteMedication(@PathVariable Long medicationId) {
		medicationService.deleteMedicationSchedule(medicationId);
		return ResponseEntity.noContent().build();
	}
}
