package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import com.ssginc8.docto.review.entity.Review;

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


	private String contents;
	private List<String> keywords;

	//신고횟수
	private Long reportCount;


	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;



	public static ReviewResponse fromEntity(Review r, List<String> kws) {
		return ReviewResponse.builder()
			.reviewId(r.getReviewId())
			.hospitalId(r.getHospital().getHospitalId())
			.hospitalName(r.getHospital().getName())
			.doctorId(r.getDoctor().getDoctorId())
			.doctorName(r.getDoctor().getUser().getName())
			.userId(r.getAuthor().getUserId())
			.userName(r.getAuthor().getName())
			.contents(r.getContents())
			.keywords(kws)
			.reportCount(r.getReportCount())
			.createdAt(r.getCreatedAt())
			.updatedAt(r.getUpdatedAt())
			.build();
	}


	public static List<ReviewResponse> fromEntities(
		List<Review> reviews,
		Function<Review, List<String>> keywordExtractor
	) {
		return reviews.stream()
			.map(r -> fromEntity(r, keywordExtractor.apply(r)))
			.toList();
	}
}


