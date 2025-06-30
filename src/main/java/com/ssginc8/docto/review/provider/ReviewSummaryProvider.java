package com.ssginc8.docto.review.provider;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.review.entity.Review;
import com.ssginc8.docto.review.provider.ReviewProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewSummaryProvider {

	private final ReviewProvider reviewProvider;

	@Transactional(readOnly = true)
	public List<String> getContents(Long hospitalId) {
		return reviewProvider
			.getHospitalReviews(hospitalId, Pageable.unpaged())
			.stream()
			.map(Review::getContents)
			.toList();
	}
}
