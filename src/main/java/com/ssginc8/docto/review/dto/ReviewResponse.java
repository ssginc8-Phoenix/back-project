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


	private String content;
	private List<String> keyword;

	//신고횟수
	private Long reportCount;


	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
