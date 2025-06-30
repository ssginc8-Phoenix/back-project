// src/main/java/com/ssginc8/docto/insurance/controller/InsuranceDocumentController.java
package com.ssginc8.docto.insurance.controller;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.ssginc8.docto.insurance.service.InsuranceDocumentService;
import com.ssginc8.docto.insurance.service.dto.UserDocumentRequest;
import com.ssginc8.docto.insurance.service.dto.DocumentResponse;
import com.ssginc8.docto.insurance.service.dto.FileDownload;

/**
 * 환자·보호자용 보험 서류 요청 API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/insurance/documents")
@RequiredArgsConstructor
public class InsuranceDocumentController {

	private final InsuranceDocumentService service;

	/** 1) 단건 요청 생성 */
	@PostMapping
	public ResponseEntity<DocumentResponse> request(
		@RequestBody UserDocumentRequest dto
	) {
		return ResponseEntity.ok(service.createRequest(dto));
	}

	/** 1-2) 배치 요청 생성 */
	@PostMapping("/batch")
	public ResponseEntity<List<DocumentResponse>> batchRequest(
		@RequestBody List<UserDocumentRequest> dtos
	) {
		return ResponseEntity.ok(service.createRequests(dtos));
	}

	/** 2) 본인 요청 페이징 조회 */
	@GetMapping
	public ResponseEntity<Page<DocumentResponse>> listOwn(
		@RequestParam Long requesterId,
		Pageable pageable
	) {
		return ResponseEntity.ok(
			service.listUserRequests(requesterId, pageable)
		);
	}

	/** 3) 단건 상태 조회 */
	@GetMapping("/{id}")
	public ResponseEntity<DocumentResponse> status(@PathVariable Long id) {
		return ResponseEntity.ok(service.status(id));
	}

	/** 4) 파일 다운로드 */
	@GetMapping("/{id}/download")
	public ResponseEntity<ByteArrayResource> download(@PathVariable Long id) {
		FileDownload dl = service.downloadFile(id);
		ByteArrayResource resource = new ByteArrayResource(dl.getData());

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + dl.getOriginalName() + "\"")
			.contentType(MediaType.parseMediaType(dl.getContentType()))
			.contentLength(dl.getSize())
			.body(resource);
	}
}
