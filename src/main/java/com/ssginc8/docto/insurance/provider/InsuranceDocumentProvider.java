// src/main/java/com/ssginc8/docto/insurance/provider/InsuranceDocumentProvider.java
package com.ssginc8.docto.insurance.provider;

import java.util.List;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.ssginc8.docto.insurance.entity.InsuranceDocument;
import com.ssginc8.docto.global.error.exception.insuranceException.DocumentNotFoundException;
import com.ssginc8.docto.insurance.repo.InsuranceDocumentRepo;

/**
 * 【Provider 계층】
 * - 순수 엔티티 조회 책임(중복 로직 제거)
 * - 없으면 즉시 예외(DocumentNotFoundException) 발생
 */
@Component
@RequiredArgsConstructor
public class InsuranceDocumentProvider {
	private final InsuranceDocumentRepo repo;

	/**
	 * 단건 조회
	 * @param id 문서 ID
	 * @return InsuranceDocument
	 * @throws DocumentNotFoundException ID가 없으면 던짐
	 */
	public InsuranceDocument getById(Long id) {
		return repo.findById(id)
			.orElseThrow(DocumentNotFoundException::new);
	}

	/**
	 * 전체 목록 조회
	 * @return List<InsuranceDocument>
	 */
	public List<InsuranceDocument> getAll() {
		return repo.findAll();
	}
}
