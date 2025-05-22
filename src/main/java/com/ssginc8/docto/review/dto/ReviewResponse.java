// src/main/java/com/ssginc8/docto/review/dto/ReviewResponse.java
package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ssginc8.docto.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponse {

	private Long reviewId;
	private String contents;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<String> keywords;

	public static ReviewResponse fromEntity(Review review, List<String> keywords) {
		ReviewResponse reviewResponse = new ReviewResponse();
		reviewResponse.reviewId = review.getReviewId();
		reviewResponse.contents      = review.getContents();
		reviewResponse.createdAt     = review.getCreatedAt();
		reviewResponse.updatedAt     = review.getUpdatedAt();
		reviewResponse.keywords      = keywords;
		return reviewResponse;
	}
}
