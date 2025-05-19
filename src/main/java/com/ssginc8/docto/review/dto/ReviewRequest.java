package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

//생성,수정 공통으로 받을 필드
@Data
@Builder
public class ReviewRequest {

	private Long appointmentId;

	private Long hospitalId;
	private Long doctorId;
	private Long userId;


	@NotBlank(message = "contents는 빈 값일 수 없습니다.")
	@Size(max = 1000, message = "contents는 최대 1000자까지 입력 가능합니다.")
	private String contents;

	private List<String> keywords;

	private LocalDateTime createdAt;

	}




