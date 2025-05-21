package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;


import com.ssginc8.docto.review.entity.Review;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ReviewResponse {

	private Long reviewId;
	private String contents;
	private List<String> keywords;
	//신고횟수
	private Long reportCount;


	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;




}


