package com.ssginc8.docto.patient.controller;

import com.ssginc8.docto.patient.dto.PatientResponse;
import com.ssginc8.docto.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 어드민 전용 환자 조회 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/patients")
public class AdminPatientController {

	private final PatientService patientService;

	/**
	 * 모든 환자 조회 API (페이징)
	 */
	@GetMapping
	public ResponseEntity<Page<PatientResponse>> getAll(Pageable pageable) {
		return ResponseEntity.ok(patientService.getAllPatients(pageable));
	}
}
