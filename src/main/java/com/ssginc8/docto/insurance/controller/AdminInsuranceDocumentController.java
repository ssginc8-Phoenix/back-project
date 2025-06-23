// src/main/java/com/ssginc8/docto/insurance/controller/AdminInsuranceDocumentController.java
package com.ssginc8.docto.insurance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import com.ssginc8.docto.insurance.service.InsuranceDocumentService;
import com.ssginc8.docto.insurance.service.dto.DocumentApprovalDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentResponseDTO;

/**
 * 관리자용 보험 서류 관리 API
 *
 * GET  /api/v1/admin/insurance/documents
 *   • 전체 요청 내역 조회
 * POST /api/v1/admin/insurance/documents/{id}/attach
 *   • 특정 요청에 대한 파일 첨부
 * PATCH /api/v1/admin/insurance/documents/{id}
 *   • 승인/반려 처리
 */
@RestController
@RequestMapping("/api/v1/admin/insurance/documents")
@RequiredArgsConstructor
public class AdminInsuranceDocumentController {

	private final InsuranceDocumentService service;

	/**
	 * 전체 요청 내역 조회
	 */
	@GetMapping
	public ResponseEntity<List<DocumentResponseDTO>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	/**
	 * 파일 첨부: 기존에 생성된 요청(ID)에만 파일을 업로드합니다.
	 */
	@PostMapping("/{id}/attach")
	public ResponseEntity<DocumentResponseDTO> attachFile(
		@PathVariable Long id,
		@RequestParam("file") MultipartFile file
	) {
		return ResponseEntity.ok(service.attachFile(id, file));
	}

	/**
	 * 승인/반려 처리
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<Void> approve(
		@PathVariable Long id,
		@RequestBody DocumentApprovalDTO dto
	) {
		service.approve(id, dto);
		return ResponseEntity.noContent().build();
	}
}
