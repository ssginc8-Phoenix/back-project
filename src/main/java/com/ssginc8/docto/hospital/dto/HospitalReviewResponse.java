package com.ssginc8.docto.hospital.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ssginc8.docto.review.dto.ReviewAllListResponse;
import com.ssginc8.docto.review.entity.Review;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HospitalReviewResponse {


	private String name;
	private Long reviewId;
	private String contents;
	private LocalDateTime createdAt;
	private Long reportCount;
	private List<String> keywords;

	public static HospitalReviewResponse fromEntity(Review review) {
		HospitalReviewResponse dto = new HospitalReviewResponse();

		dto.name = review.getUser().getName();
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
