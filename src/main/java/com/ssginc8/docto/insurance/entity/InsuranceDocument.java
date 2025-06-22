// src/main/java/com/ssginc8/docto/insurance/entity/InsuranceDocument.java
package com.ssginc8.docto.insurance.entity;

import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 【엔티티 설계 원칙】
 * 1. @NoArgsConstructor(access = AccessLevel.PROTECTED)로 protected 기본 생성자만 허용하여
 *    JPA 리플렉션용 이외의 무분별한 생성 방지
 * 2. @AllArgsConstructor, @Builder 사용 금지
 * 3. 정적 팩토리 메서드(create)를 통해 의도를 명확히, 불완전한 생성 방지
 * 4. setter 미작성: 변경이 필요한 경우 명시적 메서드(approve, reject) 제공
 * 5. @ToString 미작성: 민감 정보 노출 및 성능/순환참조 이슈 방지
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tbl_insurance_document")
public class InsuranceDocument extends BaseTimeEntity {
	/** PK: 문서 ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long documentId;

	/** 업로드된 파일(File 엔티티)과의 연관관계 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "file_id")
	private File file;

	/** 요청 상태 (REQUESTED, APPROVED, REJECTED) */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DocumentStatus status;

	/** 반려 사유 (REJECTED 상태일 때만 값 세팅) */
	@Column(length = 500)
	private String rejectionReason;

	/**
	 * 정적 팩토리 메서드: InsuranceDocument 생성
	 * - 엔티티와 DB가 맞닿아 있으므로 모든 필수 속성을 메서드에서 강제
	 * @param file 업로드된 File 엔티티
	 * @return 생성된 InsuranceDocument
	 */
	public static InsuranceDocument create(File file) {
		InsuranceDocument doc = new InsuranceDocument();
		doc.file = file;
		doc.status = DocumentStatus.REQUESTED;  // 초기 상태
		return doc;
	}

	/** 문서 승인 처리 메서드 (setter 미작성 원칙 준수) */
	public void approve() {
		this.status = DocumentStatus.APPROVED;
		this.rejectionReason = null;
	}

	/**
	 * 문서 반려 처리 메서드
	 * @param reason 반려 사유
	 */
	public void reject(String reason) {
		this.status = DocumentStatus.REJECTED;
		this.rejectionReason = reason;
	}
}
