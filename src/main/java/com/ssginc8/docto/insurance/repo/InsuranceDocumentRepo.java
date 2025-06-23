// src/main/java/com/ssginc8/docto/insurance/repo/InsuranceDocumentRepo.java
package com.ssginc8.docto.insurance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ssginc8.docto.insurance.entity.InsuranceDocument;

/**
 * InsuranceDocument 전용 JPA Repository
 */
public interface InsuranceDocumentRepo extends JpaRepository<InsuranceDocument, Long> {

	/**
	 * 환자·보호자가 생성한 요청만 페이징 조회
	 * @param requesterId 요청자 ID
	 * @param pageable    페이징 정보 (page, size, sort)
	 */
	Page<InsuranceDocument> findAllByRequesterId(Long requesterId, Pageable pageable);
}
