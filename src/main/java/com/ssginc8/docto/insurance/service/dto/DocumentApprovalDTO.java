// src/main/java/com/ssginc8/docto/insurance/service/dto/DocumentApprovalDTO.java
package com.ssginc8.docto.insurance.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 관리자 승인/반려 요청 DTO
 * - approved: true(승인) / false(반려)
 * - 반려 시 reason 필수
 */
@Getter
@Setter
public class DocumentApprovalDTO {
	@NotNull
	private Boolean approved;
	private String reason;
}
