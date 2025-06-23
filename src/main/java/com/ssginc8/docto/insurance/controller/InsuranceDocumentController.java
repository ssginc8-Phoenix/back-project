// src/main/java/com/ssginc8/docto/insurance/controller/InsuranceDocumentController.java
package com.ssginc8.docto.insurance.controller;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
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

	/**
	 * 1) 서류 요청 생성 (파일 없이)
	 */
	@PostMapping
	public ResponseEntity<DocumentResponse> request(
		@RequestBody UserDocumentRequest dto
	) {
		return ResponseEntity.ok(service.createRequest(dto));
	}

	/**
	 * 2) 요청 상태 조회 (status, rejectionReason, downloadUrl 포함)
	 */
	@GetMapping("/{id}")
	public ResponseEntity<DocumentResponse> status(@PathVariable Long id) {
		return ResponseEntity.ok(service.status(id));
	}

	/**
	 * 3) (첨부된) 파일 다운로드
	 *
	 * S3에서 직접 바이트를 읽어와, Content-Disposition / Content-Type / Content-Length 헤더를 설정 후
	 * ByteArrayResource 로 응답을 만듭니다.
	 */
	@GetMapping("/{id}/download")
	public ResponseEntity<ByteArrayResource> download(@PathVariable Long id) {
		FileDownload download = service.downloadFile(id);
		ByteArrayResource resource = new ByteArrayResource(download.getData());

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + download.getOriginalName() + "\"")
			.contentType(MediaType.parseMediaType(download.getContentType()))
			.contentLength(download.getSize())
			.body(resource);
	}
}
