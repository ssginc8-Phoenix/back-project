package com.ssginc8.docto.patient.controller;

import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.dto.PatientResponse;
import com.ssginc8.docto.patient.service.PatientService;
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
}
