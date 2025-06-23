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
 * 환자·보호자용 보험 서류 요청 API 컨트롤러
 *
 * <p>환자 또는 보호자는
 * - 파일 없이 서류 요청 생성
 * - 요청 상태 조회
 * - (관리자가 파일 첨부 후) 파일 다운로드
 * 기능을 사용할 수 있습니다.</p>
 */
@RestController
@RequestMapping("/api/v1/insurance/documents")
@RequiredArgsConstructor
public class InsuranceDocumentController {

	private final InsuranceDocumentService service;

	/**
	 * 1. 서류 요청 생성 (파일 없이)
	 *
	 * <p>사용자는 첨부 파일 없이 단순 요청 레코드만 생성합니다.</p>
	 *
	 * @param dto 요청자 ID 및 요청 메모
	 * @return 생성된 요청의 상태 정보 DTO
	 */
	@PostMapping
	public ResponseEntity<DocumentResponseDTO> request(
		@RequestBody UserDocumentRequestDTO dto
	) {
		return ResponseEntity.ok(service.createRequest(dto));
	}

	/**
	 * 2. 요청 상태 조회
	 *
	 * @param id 조회할 서류 요청 ID
	 * @return 해당 요청의 현재 상태, 반려 사유, 다운로드 URL 등을 포함한 DTO
	 */
	@GetMapping("/{id}")
	public ResponseEntity<DocumentResponseDTO> status(@PathVariable Long id) {
		return ResponseEntity.ok(service.status(id));
	}

	/**
	 * 3. (첨부된) 파일 다운로드
	 *
	 * <p>관리자가 attachFile 후, S3에 올라간 파일을 UrlResource 로 가져와
	 * Content-Disposition 헤더를 통해 브라우저에 다운로드를 유도합니다.</p>
	 *
	 * @param id 다운로드할 서류 요청 ID
	 * @return 파일 스트림을 담은 Resource 응답
	 */
	@GetMapping("/{id}/download")
	public ResponseEntity<Resource> download(@PathVariable Long id) {
		Resource resource = service.download(id);
		return ResponseEntity.ok()
			.header("Content-Disposition",
				"attachment; filename=\"insurance_document_" + id + "\"")
			.body(resource);
	}
}
