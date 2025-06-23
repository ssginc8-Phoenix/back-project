// src/main/java/com/ssginc8/docto/insurance/controller/InsuranceDocumentController.java
package com.ssginc8.docto.insurance.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssginc8.docto.insurance.service.InsuranceDocumentService;
import com.ssginc8.docto.insurance.service.dto.UserDocumentRequestDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentResponseDTO;

/**
 * 환자·보호자용 보험 서류 요청 API
 *
 * POST /api/v1/insurance/documents
 *   • 파일없이 요청만 생성
 * GET  /api/v1/insurance/documents/{id}
 *   • 상태 조회
 * GET  /api/v1/insurance/documents/{id}/download
 *   • (파일첨부 후) 다운로드
 */
@RestController
@RequestMapping("/api/v1/insurance/documents")
@RequiredArgsConstructor
public class InsuranceDocumentController {

	private final InsuranceDocumentService service;

	/**
	 * 1. 서류 요청 생성 (파일 없이)
	 */
	@PostMapping
	public ResponseEntity<DocumentResponseDTO> request(
		@RequestBody UserDocumentRequestDTO dto
	) {
		return ResponseEntity.ok(service.createRequest(dto));
	}

	/**
	 * 2. 요청 상태 조회
	 */
	@GetMapping("/{id}")
	public ResponseEntity<DocumentResponseDTO> status(@PathVariable Long id) {
		return ResponseEntity.ok(service.status(id));
	}

	/**
	 * 4. (첨부된) 파일 다운로드
	 */
	@GetMapping("/{id}/download")
   public ResponseEntity<Resource> download(@PathVariable Long id) {
		// 1) Service 에서 Resource(UrlResource) 확보
		Resource resource = service.download(id);

		// 2) 다운로드용 헤더 설정: attachment 로 강제 다운로드
		return ResponseEntity.ok()
			.header("Content-Disposition",
				"attachment; filename=\"insurance_document_" + id + "\"")
			.body(resource);
	}
}
