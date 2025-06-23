// src/main/java/com/ssginc8/docto/insurance/service/InsuranceDocumentService.java
package com.ssginc8.docto.insurance.service;

import com.ssginc8.docto.insurance.service.dto.DocumentApproval;
import com.ssginc8.docto.insurance.service.dto.UserDocumentRequest;
import com.ssginc8.docto.insurance.service.dto.DocumentResponse;
import com.ssginc8.docto.insurance.service.dto.FileDownload;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InsuranceDocumentService {

	/** 환자·보호자: 단건 요청 생성 */
	DocumentResponse createRequest(UserDocumentRequest dto);

	/** 환자·보호자: 본인 요청 페이징 조회 */
	Page<DocumentResponse> listUserRequests(Long requesterId, Pageable pageable);

	/** 환자·보호자: 배치 요청 생성 */
	List<DocumentResponse> createRequests(List<UserDocumentRequest> dtos);

	/** 관리자: 파일 첨부 */
	DocumentResponse attachFile(Long documentId, MultipartFile file);

	/** 공통: 단건 상태 조회 */
	DocumentResponse status(Long documentId);

	/** 관리자: 전체 요청 페이징 조회 */
	Page<DocumentResponse> listAll(Pageable pageable);

	/** 관리자: 승인/반려 처리 */
	void approve(Long documentId, DocumentApproval dto);

	/** 공통: 파일 다운로드 */
	FileDownload downloadFile(Long documentId);
}
