// src/main/java/com/ssginc8/docto/insurance/controller/AdminInsuranceDocumentController.java
package com.ssginc8.docto.insurance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ssginc8.docto.insurance.service.InsuranceDocumentService;
import com.ssginc8.docto.insurance.service.dto.DocumentApproval;
import com.ssginc8.docto.insurance.service.dto.DocumentResponse;

/**
 * 관리자용 보험 서류 관리 API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/admin/insurance/documents")
@RequiredArgsConstructor
public class AdminInsuranceDocumentController {

	private final InsuranceDocumentService service;

	@GetMapping
	public ResponseEntity<Page<DocumentResponse>> listByHospital(
		@RequestParam Long hospitalId,
		@PageableDefault(page = 0, size = 20, sort = "documentId", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity.ok(service.listByHospital(hospitalId, pageable));
	}

	/** 파일 첨부 */
	@PostMapping("/{id}/attach")
	public ResponseEntity<DocumentResponse> attachFile(
		@PathVariable Long id,
		@RequestParam("file") MultipartFile file
	) {
		return ResponseEntity.ok(service.attachFile(id, file));
	}

	/** 승인/반려 */
	@PatchMapping("/{id}")
	public ResponseEntity<Void> approve(
		@PathVariable Long id,
		@RequestBody DocumentApproval dto
	) {
		service.approve(id, dto);
		return ResponseEntity.noContent().build();
	}
}