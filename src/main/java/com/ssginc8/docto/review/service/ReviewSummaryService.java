package com.ssginc8.docto.review.service;


import com.ssginc8.docto.review.dto.ReviewSummaryResponse;

public interface ReviewSummaryService {
	ReviewSummaryResponse summarize(Long hospitalId);
}
