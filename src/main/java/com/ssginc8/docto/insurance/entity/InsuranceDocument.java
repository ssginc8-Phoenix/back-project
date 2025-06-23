// src/main/java/com/ssginc8/docto/insurance/entity/InsuranceDocument.java
package com.ssginc8.docto.insurance.entity;

import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 보험 서류 요청 엔티티
 *
 * • BaseTimeEntity 상속으로 생성/수정/삭제 일시 자동 관리
 * • protected 생성자 + 정적 팩토리만 허용 → 불완전한 객체 생성 방지
 * • 파일 첨부 시점에만 attach() 호출 → 강제성 보장
 * • approve(), reject() 로만 상태 변경
 */
@Entity
@Table(name = "tbl_insurance_document")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsuranceDocument extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long documentId;

	/** 요청자(환자 또는 보호자) 식별자 */
	@Column(nullable = false)
	private Long requesterId;

	/** 요청 메모(선택) */
	@Column(length = 500)
	private String note;

	/** 관리자가 첨부한 파일 정보 (optional) */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_id")
	private File file;

	/** 요청 상태 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DocumentStatus status;

	/** 반려 사유 (REJECTED 상태일 때만 값이 존재) */
	@Column(length = 500)
	private String rejectionReason;

	/**
	 * 환자·보호자용 요청 생성 메서드
	 *
	 * @param requesterId 요청자 ID
	 * @param note        요청 메모
	 * @return status=REQUESTED 인 새 엔티티
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

	/** 승인 처리: status → APPROVED, rejectionReason 초기화 */
	public void approve() {
		this.status          = DocumentStatus.APPROVED;
		this.rejectionReason = null;
	}

	/** 반려 처리: status → REJECTED, 반려 사유 세팅 */
	public void reject(String reason) {
		this.status          = DocumentStatus.REJECTED;
		this.rejectionReason = reason;
	}
}
