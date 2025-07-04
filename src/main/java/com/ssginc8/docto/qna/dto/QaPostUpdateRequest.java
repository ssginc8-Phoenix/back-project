package com.ssginc8.docto.qna.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QaPostUpdateRequest {


	@NotBlank(message = "content는 빈 값일 수 없습니다.")
	@Size(max = 500, message = "content는 최대 500자까지 입력 가능합니다.")
	private String content;

}
