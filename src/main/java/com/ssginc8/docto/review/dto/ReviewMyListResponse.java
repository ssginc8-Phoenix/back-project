package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

// 마이페이지 내가 쓴 리뷰 조회용
@Data
public class ReviewMyListResponse {


	private Long reviewId;

	private Long appointmentId;

	private String hospitalName;
	private String doctorName;

	private String content;
	private List<String> keyword;

	private Long reportCount;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
