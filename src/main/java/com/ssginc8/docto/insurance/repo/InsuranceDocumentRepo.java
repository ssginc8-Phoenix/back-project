// src/main/java/com/ssginc8/docto/insurance/repository/InsuranceDocumentRepository.java
package com.ssginc8.docto.insurance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ssginc8.docto.insurance.entity.InsuranceDocument;

/**
 * InsuranceDocument 엔티티 전용 JPA Repository
 * - 기본 CRUD 기능 제공
 */
public interface InsuranceDocumentRepo extends JpaRepository<InsuranceDocument, Long> {
}
