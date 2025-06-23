// src/main/java/com/ssginc8/docto/insurance/service/InsuranceDocumentService.java
package com.ssginc8.docto.insurance.service;

import com.ssginc8.docto.insurance.service.dto.DocumentApproval;
import com.ssginc8.docto.insurance.service.dto.UserDocumentRequest;
import com.ssginc8.docto.insurance.service.dto.DocumentResponse;
import com.ssginc8.docto.insurance.service.dto.FileDownload;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 보험 서류 비즈니스 로직 인터페이스
 *
 * • 환자·보호자 : createRequest
 * • 관리자     : attachFile, approve, listAll
 * • 공통       : getStatus, downloadFile
 */
public interface InsuranceDocumentService {

	/** 1) 환자·보호자용: 파일 없이 요청 레코드만 생성 */
	DocumentResponse createRequest(UserDocumentRequest dto);

	/** 2) 관리자용: 기존 요청에 MultipartFile 붙여 저장 */
	DocumentResponse attachFile(Long documentId, MultipartFile file);

	/** 3) 단건 상태 조회(공통) */
	DocumentResponse status(Long documentId);

	/** 4) 관리자용: 전체 요청 내역 조회 */
	List<DocumentResponse> listAll();

	/** 5) 관리자용: 승인/반려 처리 */
	void approve(Long documentId, DocumentApproval dto);

	/** 6) 공통: S3에서 직접 읽은 파일을 담은 DTO 반환 */
	FileDownload downloadFile(Long documentId);
}
