package com.ssginc8.docto.review.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewSummaryResponse {
	private final Long   hospitalId;
	private final int    reviewCount;
	private final String summary;
}

