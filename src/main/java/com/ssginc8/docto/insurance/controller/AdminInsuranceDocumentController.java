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
 * 관리자용 보험 서류 관리 API 컨트롤러
 *
 * <p>관리자는 환자·보호자가 요청한 서류 레코드에 대해
 * - 전체 내역 조회
 * - 파일 첨부
 * - 승인/반려 처리를 수행할 수 있습니다.</p>
 */
@RestController
@RequestMapping("/api/v1/admin/insurance/documents")
@RequiredArgsConstructor
public class AdminInsuranceDocumentController {

	private final InsuranceDocumentService service;

	/**
	 * 전체 요청 내역 조회
	 *
	 * @return 모든 InsuranceDocument 레코드에 대한 상태 DTO 리스트
	 */
	@GetMapping
	public ResponseEntity<List<DocumentResponseDTO>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	/**
	 * 특정 요청에 파일 첨부
	 *
	 * <p>1) 클라이언트로부터 MultipartFile을 수신하고
	 * 2) S3 업로드 → File 엔티티 생성/저장 → InsuranceDocument 에 첨부 → 상태 DTO 반환</p>
	 *
	 * @param id   파일을 첨부할 서류 요청 ID
	 * @param file 업로드할 보험 서류 파일
	 * @return 첨부 후 업데이트된 상태 정보
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
	 *
	 * <p>DocumentApprovalDTO.approved 값이 true 이면 APPROVED,
	 * false 이면 REJECTED 처리 후 저장합니다.</p>
	 *
	 * @param id  승인/반려 대상 서류 요청 ID
	 * @param dto 승인 여부 및 반려 사유
	 * @return 처리 후 응답 본문 없음(204 No Content)
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
