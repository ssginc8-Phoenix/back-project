package com.ssginc8.docto.patient.controller;

import java.util.List;

import com.ssginc8.docto.guardian.dto.GuardianResponse;
import com.ssginc8.docto.guardian.service.GuardianService;
import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.dto.PatientResponse;
import com.ssginc8.docto.patient.service.PatientService;
import com.ssginc8.docto.user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 환자 관련 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientController {

	private final PatientService patientService;
	private final GuardianService guardianService;
	private final UserService userService;

	/**
	 * 환자 등록 API
	 */
	@PostMapping
	public ResponseEntity<Long> create(@RequestBody PatientRequest dto) {
		return ResponseEntity.ok(patientService.createPatient(dto));
	}

	/**
	 * 환자 삭제 API (soft delete)
	 */
	@DeleteMapping("/{patientId}")
	public ResponseEntity<Void> delete(@PathVariable Long patientId) {
		patientService.deletePatient(patientId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 보호자 목록 조회 API
	 */
	@GetMapping("/{patientId}/guardians")
	public ResponseEntity<List<GuardianResponse>> getGuardians(@PathVariable Long patientId) {
		List<GuardianResponse> guardians = guardianService.getGuardiansByPatientId(patientId);
		return ResponseEntity.ok(guardians);
	}

	@GetMapping("/me")
	public ResponseEntity<PatientResponse> getMyPatientInfo() {
		Long userId = userService.getUserFromUuid().getUserId();
		PatientResponse patientInfo = patientService.getPatientByUserId(userId);
		return ResponseEntity.ok(patientInfo);
	}
}
