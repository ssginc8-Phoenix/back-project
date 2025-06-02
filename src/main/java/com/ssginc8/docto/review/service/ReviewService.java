package com.ssginc8.docto.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssginc8.docto.review.dto.ReviewAllListResponse;
import com.ssginc8.docto.review.dto.ReviewCreateRequest;
import com.ssginc8.docto.review.dto.ReviewMyListResponse;
import com.ssginc8.docto.review.dto.ReviewResponse;
import com.ssginc8.docto.review.dto.ReviewUpdateRequest;

public interface ReviewService {

	//리뷰 생성
	Long createReview(ReviewCreateRequest request, Long userId);

	//리뷰 수정
	ReviewResponse updateReview( ReviewUpdateRequest request, Long reviewId);

	//리뷰 삭제
	void deleteReview(Long reviewId);

	// 내 리뷰 목록 조회
	Page<ReviewMyListResponse> getMyReviews(Long userId, Pageable pageable);

	// // 병원 리뷰 목록 조회
	// Page<ReviewAllListResponse> getHospitalReviews(Long hospitalId, Pageable pageable);

	// 관리자용 리뷰 전체 조회
	Page<ReviewAllListResponse> getAllReviews(Pageable pageable);


	//리뷰 신고
	void reportReview(Long reviewId);


}







