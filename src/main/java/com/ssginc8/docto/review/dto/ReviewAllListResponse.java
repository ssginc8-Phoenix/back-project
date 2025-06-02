package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ssginc8.docto.review.entity.Review;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ReviewAllListResponse {

	private Long reviewId;
	private Long hospitalId;
	private String hospitalName;
	private String contents;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long reportCount;

	private List<String> keywords;


	public static ReviewAllListResponse fromEntity(Review review) {
		ReviewAllListResponse dto = new ReviewAllListResponse();
		dto.reviewId    = review.getReviewId();
		dto.contents    = review.getContents();
		dto.createdAt   = review.getCreatedAt();
		dto.updatedAt   = review.getUpdatedAt();
		dto.reportCount = review.getReportCount();
		dto.keywords    = review.getKeywords().stream()
			.map(Enum::name)
			.toList();

		// 연관된 병원 정보
		if (review.getHospital() != null) {
			dto.hospitalId   = review.getHospital().getHospitalId();
			dto.hospitalName = review.getHospital().getName();
		}
		return dto;
	}
}
