// src/main/java/com/ssginc8/docto/insurance/service/dto/DocumentRequestDTO.java
package com.ssginc8.docto.insurance.service.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

/**
 * 서류 요청 시 클라이언트에서 전달되는 DTO
 * - 파일 필수(@NotNull)
 */
@Getter
@Setter
public class DocumentRequestDTO {
	@NotNull
	private MultipartFile file;
}
