// src/main/java/com/ssginc8/docto/insurance/service/InsuranceDocumentService.java
package com.ssginc8.docto.insurance.service;

import com.ssginc8.docto.insurance.service.dto.DocumentRequestDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentResponseDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentApprovalDTO;
import org.springframework.core.io.Resource;
import java.util.List;

/**
 * 보험 서류 비즈니스 로직 인터페이스
 * - 요청, 상태조회, 전체조회, 승인/반려, 다운로드 기능 정의
 * - 서비스 계층에서는 오직 비즈니스 흐름에 집중
 */
public interface InsuranceDocumentService {
	DocumentResponseDTO request(DocumentRequestDTO dto);
	DocumentResponseDTO status(Long documentId);
	List<DocumentResponseDTO> listAll();
	void approve(Long documentId, DocumentApprovalDTO dto);
	Resource download(Long documentId);
}
