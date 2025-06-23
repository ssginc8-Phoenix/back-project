// src/main/java/com/ssginc8/docto/insurance/service/dto/DocumentApproval.java
package com.ssginc8.docto.insurance.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 관리자용 승인/반려 처리 요청 DTO
 */
@Getter
@Setter
public class DocumentApproval {
	/** true=승인, false=반려 */
	@NotNull
	private Boolean approved;

	/** 반려 사유 (approved=false일 때 필수) */
	private String reason;
}
