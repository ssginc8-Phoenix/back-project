package com.ssginc8.docto.medication.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssginc8.docto.medication.dto.*;
import com.ssginc8.docto.medication.service.MedicationService;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medications")
@RequiredArgsConstructor
public class MedicationController {

	private final MedicationService medicationService;
	private final UserProvider userProvider;

	@GetMapping("/me/logs")
	public ResponseEntity<Page<MedicationLogResponse>> getLogsByCurrentUser(Pageable pageable) {
		Page<MedicationLogResponse> response = medicationService.getMedicationLogsByCurrentUser(pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/me/schedules")
	public ResponseEntity<List<MedicationScheduleResponse>> getSchedulesByCurrentUser() {
		List<MedicationScheduleResponse> response = medicationService.getMedicationSchedulesByCurrentUser();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{medicationId}")
	public ResponseEntity<MedicationScheduleResponse> getSchedule(
		@PathVariable Long medicationId
	) {
		MedicationScheduleResponse dto = medicationService.getMedicationScheduleById(medicationId);
		return ResponseEntity.ok(dto);
	}

	@PostMapping
	public ResponseEntity<Void> registerSchedule(@RequestBody MedicationScheduleRequest request) {
		medicationService.registerMedicationSchedule(request);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{medicationId}/complete")
	public ResponseEntity<Void> completeMedication(@PathVariable Long medicationId, @RequestBody MedicationCompleteRequest request) {
		medicationService.completeMedication(medicationId, request);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{medicationId}")
	public ResponseEntity<Void> updateTime(@PathVariable Long medicationId,
		@RequestBody MedicationUpdateRequest request) {
		medicationService.updateMedicationTime(medicationId, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{medicationId}")
	public ResponseEntity<Void> deleteMedication(@PathVariable Long medicationId) {
		medicationService.deleteMedicationSchedule(medicationId);
		return ResponseEntity.noContent().build();
	}
}
