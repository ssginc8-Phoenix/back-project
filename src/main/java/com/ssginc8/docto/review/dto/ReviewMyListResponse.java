// src/main/java/com/ssginc8/docto/review/dto/ReviewMyListResponse.java
package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ssginc8.docto.review.entity.KeywordType;
import com.ssginc8.docto.review.entity.Review;
import lombok.Getter;

@Getter
public class ReviewMyListResponse {

	private Long reviewId;
	private String contents;
	private LocalDateTime createdAt;
	private List<String> keywords;

	public static ReviewMyListResponse fromEntity(Review reviews) {

			ReviewMyListResponse dto = new ReviewMyListResponse();
			dto.reviewId   = reviews.getReviewId();
			dto.contents   = reviews.getContents();
			dto.createdAt  = reviews.getCreatedAt();
			// Review 엔티티에 @ElementCollection 으로 keywords가 Set<KeywordType>으로 저장되어 있다고 가정
			dto.keywords = reviews.getKeywords().stream()
				.map(KeywordType::name)   // Enum → String
				.toList();
			return dto;
		}
	}

