package com.ssginc8.docto.review.service;


import java.util.List;

import com.ssginc8.docto.review.dto.ReviewCreateRequest;
import com.ssginc8.docto.review.dto.ReviewUpdateRequest;
import com.ssginc8.docto.review.entity.KeywordType;
import com.ssginc8.docto.review.entity.Review;

//쓰기(생성·수정·삭제)” 기능을 처리

public interface ReviewService {

	//리뷰 생성
	Long createReview(ReviewCreateRequest request, Long userId);

	//리뷰 수정
	void updateReview(Long reviewId, ReviewUpdateRequest request);

	//리뷰 삭제
	void deleteReview(Long reviewId);

	List<Review> getMyReviews(Long userId);
}
