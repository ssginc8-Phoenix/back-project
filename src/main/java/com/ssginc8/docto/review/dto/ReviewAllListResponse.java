package com.ssginc8.docto.review.dto;

import java.util.List;

import com.ssginc8.docto.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class ReviewAllListResponse {


	private Long hospitalId;
	private Long reviewId;
	private String contents;
	private List<String> keywords;
	private Long reportCount;


	public static ReviewAllListResponse fromEntity(Review r, List<String> kws) {
		return new ReviewAllListResponse(
			r.getHospital().getHospitalId(),
			r.getReviewId(),
			r.getContents(),
			kws,
			r.getReportCount()
		);
	}
}
