// src/main/java/com/ssginc8/docto/insurance/service/dto/DocumentRequestDTO.java
package com.ssginc8.docto.insurance.service.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

/**
 * 보험 서류 요청 시 클라이언트에서 전달되는 DTO
 * • 파일(MultipartFile) 필수
 */
@Getter
@Setter
public class DocumentRequestDTO {

	/** 업로드할 보험 서류 파일 */
	@NotNull
	private MultipartFile file;
}
