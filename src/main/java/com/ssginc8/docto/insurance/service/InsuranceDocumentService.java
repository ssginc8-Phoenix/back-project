// src/main/java/com/ssginc8/docto/insurance/service/InsuranceDocumentService.java
package com.ssginc8.docto.insurance.service;

import com.ssginc8.docto.insurance.service.dto.DocumentRequestDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentResponseDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentApprovalDTO;
import com.ssginc8.docto.insurance.service.dto.UserDocumentRequestDTO;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 보험 서류 비즈니스 로직 인터페이스
 *
 * • 환자·보호자: 요청만 생성 (createRequest)
 * • 관리자  : 파일 첨부 (attachFile), 승인/반려, 조회, 다운로드
 */
public interface InsuranceDocumentService {

	/**
	 * 환자·보호자용: 파일 없이 서류 요청 레코드만 생성
	 *
	 * @param dto 요청자 ID, 메모 등을 담은 DTO
	 * @return 생성된 요청의 상태 정보
	 */
	DocumentResponseDTO createRequest(UserDocumentRequestDTO dto);

	/**
	 * 관리자용: 기존 요청에 파일을 첨부하여 S3 업로드 + 엔티티 연결
	 *
	 * @param documentId 서류 요청 ID
	 * @param file       업로드할 MultipartFile
	 * @return 파일 첨부 후 상태 정보
	 */
	DocumentResponseDTO attachFile(Long documentId, MultipartFile file);

	/**
	 * (공통) 단건 상태 조회
	 *
	 * @param documentId 요청 ID
	 * @return 현재 상태, 반려 사유, 다운로드 URL 등
	 */
	DocumentResponseDTO status(Long documentId);

	/**
	 * 관리자용: 전체 요청 내역 조회
	 */
	List<DocumentResponseDTO> listAll();

	/**
	 * 관리자용: 승인 또는 반려 처리
	 *
	 * @param documentId 요청 ID
	 * @param dto        approved=true(승인) / false(반려), 반려 시 reason 필수
	 */
	void approve(Long documentId, DocumentApprovalDTO dto);

	/**
	 * (공통) 파일 다운로드용 Resource 반환
	 *
	 * @param documentId 요청 ID
	 * @return UrlResource 형태의 파일 스트림
	 */
	Resource download(Long documentId);
}
