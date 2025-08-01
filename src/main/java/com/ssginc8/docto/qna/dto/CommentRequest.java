package com.ssginc8.docto.qna.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequest {

	@NotBlank(message = "content는 빈 값일 수 없습니다.")
	@Size(max = 500, message = "content는 최대 500자 까지 가능합니다.")
	private String content;


}
