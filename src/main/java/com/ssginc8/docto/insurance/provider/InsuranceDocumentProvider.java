// src/main/java/com/ssginc8/docto/insurance/provider/InsuranceDocumentProvider.java
package com.ssginc8.docto.insurance.provider;

import java.util.List;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.ssginc8.docto.insurance.entity.InsuranceDocument;
import com.ssginc8.docto.global.error.exception.insuranceException.DocumentNotFoundException;
import com.ssginc8.docto.insurance.repo.InsuranceDocumentRepo;

/**
 * Provider 계층: 순수 Entity 조회 책임
 * • findById 시 예외 처리 일원화
 * • findAll 로 전체 조회 제공
 */
@Component
@RequiredArgsConstructor
public class InsuranceDocumentProvider {

	private final InsuranceDocumentRepo repo;

	/**
	 * 단건 조회
	 *
	 * @param id 조회할 documentId
	 * @return InsuranceDocument
	 * @throws DocumentNotFoundException ID가 없으면 던짐
	 */
	public InsuranceDocument getById(Long id) {
		return repo.findById(id)
			.orElseThrow(DocumentNotFoundException::new);
	}

	/**
	 * 전체 목록 조회
	 *
	 * @return List<InsuranceDocument>
	 */
	public List<InsuranceDocument> getAll() {
		return repo.findAll();
	}
}
