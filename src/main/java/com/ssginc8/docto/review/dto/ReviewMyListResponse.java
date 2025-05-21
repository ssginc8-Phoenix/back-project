package com.ssginc8.docto.review.dto;

import java.util.List;
import com.ssginc8.docto.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewMyListResponse {

	private Long reviewId;
	private String contents;
	private List<String> keywords;


	public static ReviewMyListResponse fromEntity(Review r, List<String> keywords) {
		return new ReviewMyListResponse(
			r.getReviewId(),
			r.getContents(),
			keywords
		);
	}
}
