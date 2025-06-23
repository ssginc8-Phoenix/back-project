// src/main/java/com/ssginc8/docto/insurance/service/dto/UserDocumentRequestDTO.java
package com.ssginc8.docto.insurance.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 환자·보호자용 요청 DTO
 *
 * • requesterId – 요청자 식별자
 * • note        – 요청 메모(선택)
 */
@Getter
@Setter
public class UserDocumentRequestDTO {
	@NotNull
	private Long requesterId;
	private String note;
}
