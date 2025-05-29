package com.ssginc8.docto.guardian.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.guardian.dto.GuardianStatusRequest;
import com.ssginc8.docto.guardian.dto.PatientSummaryResponse;
import com.ssginc8.docto.guardian.service.GuardianService;

import lombok.RequiredArgsConstructor;

/**
 * 보호자 관련 API 컨트롤러
 * - 권한 수락/거절
 * - 권한 해제
 * - 보호자 수락 환자 목록 조회
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guardians")
public class GuardianController {

	private final GuardianService guardianService;

	/**
	 * 보호자 권한 요청 수락/거절 API
	 */
	@PostMapping("/request/{requestId}")
	public ResponseEntity<Void> respond(
		@PathVariable Long requestId,
		@RequestBody GuardianStatusRequest request) {

		guardianService.updateStatus(requestId,
			request.getInviteCode(),
			request.getStatus());
		return ResponseEntity.ok().build();
	}


	/**
	 * 보호자-환자 매핑 삭제 API
	 */
	@DeleteMapping("/{userId}/patients/{patientId}")
	public ResponseEntity<Void> deleteMapping(@PathVariable Long userId, @PathVariable Long patientId) {
		guardianService.deleteMapping(userId, patientId);
		return ResponseEntity.noContent().build();
	}


	/**
	 * 보호자가 가진 환자 목록 조회 API
	 */
	@GetMapping("/me/patients")
	public ResponseEntity<List<PatientSummaryResponse>> getAllAcceptedMappings() {
		List<PatientSummaryResponse> patients = guardianService.getAllAcceptedMappings();
		return ResponseEntity.ok(patients);
	}
}