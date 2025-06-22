// src/main/java/com/ssginc8/docto/insurance/controller/AdminInsuranceDocumentController.java
package com.ssginc8.docto.insurance.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssginc8.docto.insurance.service.InsuranceDocumentService;
import com.ssginc8.docto.insurance.service.dto.DocumentResponseDTO;

/**
 * 관리자용 보험 서류 내역 조회 API
 * GET /api/v1/admin/insurance/documents
 */
@RestController
@RequestMapping("/api/v1/admin/insurance/documents")
@RequiredArgsConstructor
public class AdminInsuranceDocumentController {
	private final InsuranceDocumentService service;

	@GetMapping
	public ResponseEntity<List<DocumentResponseDTO>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}
}
