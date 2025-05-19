package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

//단건 조회 시 반환할 필드
@Data
@Builder
public class ReviewResponse {

	private Long reviewId;

	private Long hospitalId;
	private String hospitalName;

	private Long doctorId;
	private String doctorName;

	private Long userId;
	private String userName;


	@NotBlank(message = "contents는 빈 값일 수 없습니다.")
	@Size(max = 1000, message = "contents는 최대 1000자까지 입력 가능합니다.")
	private String contents;


	private List<String> keywords;

	//신고횟수
	private Long reportCount;


	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
