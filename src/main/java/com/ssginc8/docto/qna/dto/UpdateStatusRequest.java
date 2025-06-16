package com.ssginc8.docto.qna.dto;

import com.ssginc8.docto.qna.entity.QaStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateStatusRequest {
	@NotNull
	private QaStatus status;
}
