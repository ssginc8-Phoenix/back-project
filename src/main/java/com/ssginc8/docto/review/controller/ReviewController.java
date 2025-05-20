package com.ssginc8.docto.review.controller;

import java.util.List;
import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssginc8.docto.review.dto.ReviewCreateRequest;
import com.ssginc8.docto.review.dto.ReviewResponse;
import com.ssginc8.docto.review.dto.ReviewUpdateRequest;
import com.ssginc8.docto.review.entity.Review;
import com.ssginc8.docto.review.entity.ReviewKeyword;
import com.ssginc8.docto.review.repository.ReviewKeywordRepo;
import com.ssginc8.docto.review.service.ReviewServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

	private final ReviewServiceImpl reviewService;
	private final ReviewKeywordRepo reviewKeywordRepo;

	// 리뷰 작성
	@PostMapping("/reviews")
	public ResponseEntity<Long> create(@RequestBody ReviewCreateRequest dto) {
		Long id = reviewService.createReview(dto, dto.getUserId());
		return ResponseEntity.ok(id);
	}


	// 리뷰 수정
	@PatchMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> update(
		@PathVariable Long reviewId,
		@RequestBody ReviewUpdateRequest dto
	) {
		reviewService.updateReview(reviewId, dto);
		return ResponseEntity.noContent().build();
	}

	// 리뷰 삭제
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> delete(@PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.noContent().build();
	}

	// 나의 리뷰 조회
	@GetMapping("/users/me/reviews")
	public ResponseEntity<List<ReviewResponse>> getMyReviews(@RequestParam Long userId) {
		List<Review> reviews = reviewService.getMyReviews(userId);

		Function<Review, List<String>> kwExtractor = r ->
			reviewKeywordRepo.findByReview(r)
				.stream()
				.map(ReviewKeyword::getKeywords)
				.map(Enum::name)
				.toList();

		List<ReviewResponse> dtoList = ReviewResponse.fromEntities(reviews, kwExtractor);
		return ResponseEntity.ok(dtoList);
	}

	// 병원관리자용 전체 리뷰 조회
	@GetMapping("/reviews")
	public ResponseEntity<List<ReviewResponse>> getAllReviews() {
		List<Review> reviews = reviewService.getAllReviews();

		// 리뷰 → 키워드 문자열 리스트 추출 함수
		Function<Review, List<String>> kwExtractor = r ->
			reviewKeywordRepo.findByReview(r)
				.stream()
				.map(ReviewKeyword::getKeywords)
				.map(Enum::name)
				.toList();

		List<ReviewResponse> dtoList = ReviewResponse.fromEntities(reviews, kwExtractor);
		return ResponseEntity.ok(dtoList);
	}
}
