package com.ssginc8.docto.guardian.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssginc8.docto.guardian.dto.GuardianInviteRequest;
import com.ssginc8.docto.guardian.dto.GuardianInviteResponse;
import com.ssginc8.docto.guardian.dto.GuardianStatusRequest;
import com.ssginc8.docto.guardian.dto.PatientSummaryResponse;
import com.ssginc8.docto.guardian.service.GuardianService;
import com.ssginc8.docto.user.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guardians")
public class GuardianController {

	private final GuardianService guardianService;
	private final UserServiceImpl userService;

	@PatchMapping("/request/{requestId}")
	public ResponseEntity<Void> respond(
		@PathVariable Long requestId,
		@RequestBody GuardianStatusRequest request) {

		guardianService.updateStatus(requestId, request.getInviteCode(), request.getStatus());
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/respond")
	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
	public ResponseEntity<Void> respondByInviteCode(
		@RequestBody GuardianStatusRequest request) {
		guardianService.updateStatusByInviteCode(request.getInviteCode(), request.getStatus());
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{userId}/patients/{patientId}")
	public ResponseEntity<Void> deleteMapping(@PathVariable Long userId, @PathVariable Long patientId) {
		guardianService.deleteMapping(userId, patientId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{patientId}/invite")
	public ResponseEntity<GuardianInviteResponse> inviteGuardian(
		@PathVariable Long patientId,
		@RequestBody GuardianInviteRequest request) {

		return ResponseEntity.ok(guardianService.inviteGuardian(patientId, request.getGuardianEmail()));
	}

	@GetMapping("/me/patients")
	public ResponseEntity<List<PatientSummaryResponse>> getAllAcceptedMappings() {
		Long guardianId = userService.getUserFromUuid().getUserId();
		List<PatientSummaryResponse> patients = guardianService.getAllAcceptedMappings(guardianId);
		return ResponseEntity.ok(patients);
	}
}
