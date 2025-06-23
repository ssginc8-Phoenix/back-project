// src/main/java/com/ssginc8/docto/insurance/service/InsuranceDocumentService.java
package com.ssginc8.docto.insurance.service;

import com.ssginc8.docto.insurance.service.dto.DocumentResponseDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentApprovalDTO;
import com.ssginc8.docto.insurance.service.dto.UserDocumentRequestDTO;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 보험 서류 비즈니스 로직 인터페이스
 *
 * • 환자·보호자: createRequest
 * • 관리자    : attachFile, approve, listAll
 * • 공통      : status, download
 */
public interface InsuranceDocumentService {

	DocumentResponseDTO createRequest(UserDocumentRequestDTO dto);

	DocumentResponseDTO attachFile(Long documentId, MultipartFile file);

	DocumentResponseDTO status(Long documentId);

	List<DocumentResponseDTO> listAll();

	void approve(Long documentId, DocumentApprovalDTO dto);

	Resource download(Long documentId);
}
