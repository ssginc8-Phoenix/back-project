// src/main/java/com/ssginc8/docto/insurance/service/dto/UserDocumentRequest.java
package com.ssginc8.docto.insurance.service.dto;

import com.ssginc8.docto.insurance.entity.DocumentType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 환자·보호자용: 파일 없이 요청만 할 때 사용하는 DTO
 */
@Getter
@Setter
public class UserDocumentRequest {
	/** 요청자 식별자 (토큰에서 가져와도 무방) */
	@NotNull
	private Long requesterId;

	/** 발급받을 병원 ID */
	@NotNull
	private Long hospitalId;

	/** 문서 종류   */
	@NotNull private DocumentType type;

	/** 요청 메모(선택) */
	private String note;
}
