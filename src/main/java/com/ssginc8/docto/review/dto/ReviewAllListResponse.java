package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ssginc8.docto.review.entity.Review;
import lombok.Getter;

@Getter
public class ReviewAllListResponse {

	private Long reviewId;
	private String contents;
	private LocalDateTime createdAt;
	private Long reportCount;
	private List<String> keywords;

	public static ReviewAllListResponse fromEntity(Review review) {
		ReviewAllListResponse dto = new ReviewAllListResponse();
		dto.reviewId    = review.getReviewId();
		dto.contents    = review.getContents();
		dto.createdAt   = review.getCreatedAt();
		dto.reportCount = review.getReportCount();
		dto.keywords    = review.getKeywords().stream()
			.map(Enum::name)
			.toList();
		return dto;
	}
}
