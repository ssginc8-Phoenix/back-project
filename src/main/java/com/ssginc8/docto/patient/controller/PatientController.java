package com.ssginc8.docto.patient.controller;

import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.dto.PatientResponse;
import com.ssginc8.docto.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 환자 관련 API를 처리하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientController {

	private final PatientService patientService;

	/**
	 * 환자 등록 API
	 */
	@PostMapping
	public ResponseEntity<PatientResponse> create(@RequestBody PatientRequest dto) {
		return ResponseEntity.ok(patientService.createPatient(dto));
	}

	/**
	 * 모든 환자 조회 API
	 */
	@GetMapping
	public ResponseEntity<List<PatientResponse>> getAll() {
		return ResponseEntity.ok(patientService.getAllPatients());
	}

	/**
	 * 환자 삭제 API (소프트 삭제)
	 */
	@DeleteMapping("/{patientId}")
	public ResponseEntity<Void> delete(@PathVariable Long patientId) {
		patientService.deletePatient(patientId);
		return ResponseEntity.noContent().build();
	}
}