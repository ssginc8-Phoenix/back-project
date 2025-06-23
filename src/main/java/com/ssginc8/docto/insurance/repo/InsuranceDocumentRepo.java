// src/main/java/com/ssginc8/docto/insurance/repo/InsuranceDocumentRepo.java
package com.ssginc8.docto.insurance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ssginc8.docto.insurance.entity.InsuranceDocument;

/**
 * InsuranceDocument 전용 JPA Repository
 * • 기본 CRUD 메서드 제공
 */
public interface InsuranceDocumentRepo extends JpaRepository<InsuranceDocument, Long> { }
