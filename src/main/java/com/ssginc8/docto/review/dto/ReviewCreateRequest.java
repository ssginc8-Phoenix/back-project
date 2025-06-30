package com.ssginc8.docto.review.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ReviewCreateRequest {

	private Long appointmentId;
	private Long userId;
	private Long hospitalId;
	private Long doctorId;

	@NotBlank(message = "키워드를 최소 3개 이상 선택해야 합니다.")
	@Size(min = 3, max = 8, message = "키워드는 3~8개 사이로 선택 가능합니다.")
	private List<String> keywords;


	@NotBlank(message = "contents는 빈 값일 수 없습니다.")
	@Size(max = 1000, message = "contents는 최대 1000자까지 입력 가능합니다.")
	private String contents;




}
