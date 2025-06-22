// src/main/java/com/ssginc8/docto/insurance/controller/InsuranceDocumentController.java
package com.ssginc8.docto.insurance.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.ssginc8.docto.insurance.service.InsuranceDocumentService;
import com.ssginc8.docto.insurance.service.dto.DocumentRequestDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentResponseDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentApprovalDTO;

/**
 * 환자/보호자용 보험 서류 API
 * 1. POST   /api/v1/insurance/documents         - 서류 요청
 * 2. GET    /api/v1/insurance/documents/{id}    - 상태 조회
 * 3. PATCH  /api/v1/insurance/documents/{id}    - 승인/반려
 * 4. GET    /api/v1/insurance/documents/{id}/download - 다운로드
 */
@RestController
@RequestMapping("/api/v1/insurance/documents")
@RequiredArgsConstructor
public class InsuranceDocumentController {
	private final InsuranceDocumentService service;

	@PostMapping
	public ResponseEntity<DocumentResponseDTO> request(@ModelAttribute DocumentRequestDTO dto) {
		return ResponseEntity.ok(service.request(dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DocumentResponseDTO> status(@PathVariable Long id) {
		return ResponseEntity.ok(service.status(id));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> approve(
		@PathVariable Long id,
		@RequestBody DocumentApprovalDTO dto
	) {
		service.approve(id, dto);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/download")
	public ResponseEntity<Resource> download(@PathVariable Long id) {
		Resource resource = service.download(id);
		return ResponseEntity.ok()
			.header("Content-Disposition", "attachment; filename=\"insurance_document_" + id + "\"")
			.body(resource);
	}
}
