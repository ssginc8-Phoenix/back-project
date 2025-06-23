// src/main/java/com/ssginc8/docto/insurance/service/dto/UserDocumentRequestDTO.java
package com.ssginc8.docto.insurance.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 환자·보호자용: 파일 없이 서류 요청만 처리하는 DTO
 */
@Getter
@Setter
public class UserDocumentRequestDTO {
	/** 요청자 식별자 (토큰에서 가져올 수도 있습니다) */
	@NotNull
	private Long requesterId;

	/** 요청 메모(선택) */
	private String note;
}
