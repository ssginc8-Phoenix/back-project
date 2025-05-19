package com.ssginc8.docto.patient.controller;

import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.dto.PatientResponse;
import com.ssginc8.docto.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientController {

	private final PatientService patientService;

	@PostMapping
	public ResponseEntity<PatientResponse> create(@RequestBody PatientRequest dto) {
		return ResponseEntity.ok(patientService.createPatient(dto));
	}

	@GetMapping
	public ResponseEntity<List<PatientResponse>> getAll() {
		return ResponseEntity.ok(patientService.getAllPatients());
	}

	@DeleteMapping("/{patientId}")
	public ResponseEntity<Void> delete(@PathVariable Long patientId) {
		patientService.deletePatient(patientId);
		return ResponseEntity.noContent().build();
	}
}