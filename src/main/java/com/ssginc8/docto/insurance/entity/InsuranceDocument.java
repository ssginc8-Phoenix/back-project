// src/main/java/com/ssginc8/docto/insurance/entity/InsuranceDocument.java
package com.ssginc8.docto.insurance.entity;

import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 보험 서류 엔티티
 *
 * • protected 기본 생성자
 * • setter 제거, 변경은 도메인 메서드로만
 * • 정적 팩토리만을 통해 생성
 */
@Entity
@Table(name = "tbl_insurance_document")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsuranceDocument extends BaseTimeEntity {

	/** PK: 서류 요청 고유 ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long documentId;

	/** 요청자 식별자 (환자·보호자용 요청 시 필요) */
	@Column(nullable = false)
	private Long requesterId;

	/** 요청 메모(선택) */
	@Column(length = 500)
	private String note;

	/** 파일 첨부 시점에 설정되는 File 엔티티 */
	@ManyToOne(fetch = FetchType.LAZY)
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
	 * 환자·보호자용 요청 생성 팩토리
	 *
	 * @param requesterId 요청자 ID
	 * @param note        요청 메모(선택)
	 * @return 생성된 InsuranceDocument (file 없음, status=REQUESTED)
	 */
	public static InsuranceDocument createRequest(Long requesterId, String note) {
		InsuranceDocument doc = new InsuranceDocument();
		doc.requesterId = requesterId;
		doc.note        = note;
		doc.status      = DocumentStatus.REQUESTED;
		return doc;
	}

	/**
	 * 관리자용 파일 첨부 메서드
	 *
	 * @param file S3 업로드 후 생성된 File 엔티티
	 */
	public void attach(File file) {
		this.file = file;
	}

	/**
	 * 기존 파일 업로드 시 즉시 생성 팩토리 (필요시 유지)
	 *
	 * @param file File 엔티티
	 * @return 생성된 InsuranceDocument (status=REQUESTED)
	 */
	public static InsuranceDocument createWithFile(File file) {
		InsuranceDocument doc = new InsuranceDocument();
		doc.file   = file;
		doc.status = DocumentStatus.REQUESTED;
		return doc;
	}

	/** 승인 처리 */
	public void approve() {
		this.status          = DocumentStatus.APPROVED;
		this.rejectionReason = null;
	}

	/**
	 * 반려 처리
	 *
	 * @param reason 반려 사유
	 */
	public void reject(String reason) {
		this.status          = DocumentStatus.REJECTED;
		this.rejectionReason = reason;
	}
}
