package com.ssginc8.docto.review.controller;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssginc8.docto.review.dto.ReviewAllListResponse;
import com.ssginc8.docto.review.dto.ReviewCreateRequest;
import com.ssginc8.docto.review.dto.ReviewMyListResponse;
import com.ssginc8.docto.review.dto.ReviewResponse;
import com.ssginc8.docto.review.dto.ReviewUpdateRequest;
import com.ssginc8.docto.review.service.ReviewService;


import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

	 private final ReviewService reviewService;


	// 리뷰 생성
	@PostMapping("/reviews")
	public ResponseEntity<Long> create(@RequestBody ReviewCreateRequest dto) {
		Long id = reviewService.createReview(dto, dto.getUserId());
		//리뷰 아이디 반환
		return ResponseEntity.ok(id);
	}


	// 리뷰 수정
	@PatchMapping("/reviews/{reviewId}")
	public ResponseEntity<ReviewResponse> update(
		@PathVariable Long reviewId,
		@RequestBody ReviewUpdateRequest dto) {
		ReviewResponse updated = reviewService.updateReview(dto, reviewId);
		return ResponseEntity.ok(updated);
	}


	// 리뷰 삭제
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> delete(@PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.noContent().build();
	}



	//나의 예약 조회
	@GetMapping("/users/me/reviews")
	public ResponseEntity<Page<ReviewMyListResponse>> getMyReviews(@RequestParam("userId") Long userId, Pageable pageable
	) {Page<ReviewMyListResponse> page = reviewService.getMyReviews(userId, pageable);
		return ResponseEntity.ok(page);
	}


	//병원 리스트 에서 전체 리뷰 조회
	@GetMapping("/hospitals/{hospitalId}/reviews")
	public ResponseEntity<Page<ReviewAllListResponse>> getAllReviews(@PathVariable Long hospitalId, Pageable pageable
	) {Page<ReviewAllListResponse> page = reviewService.getAllReviews(hospitalId,pageable);
		return ResponseEntity.ok(page);
	}

	// 신고 엔드포인트
	@PostMapping("/reviews/{reviewId}/report")
	public ResponseEntity<Void> report(@PathVariable Long reviewId) {
		reviewService.reportReview(reviewId);
		return ResponseEntity.ok().build();
	}

}







