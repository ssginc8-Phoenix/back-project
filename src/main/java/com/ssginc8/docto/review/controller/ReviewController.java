package com.ssginc8.docto.review.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssginc8.docto.review.dto.ReviewCreateRequest;
import com.ssginc8.docto.review.dto.ReviewUpdateRequest;
import com.ssginc8.docto.review.entity.Review;
import com.ssginc8.docto.review.service.ReviewServiceImpl;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

	private final ReviewServiceImpl reviewService;

	// 리뷰 작성 POST: /api/v1/reviews
	@PostMapping
	public ResponseEntity<Long> create(@RequestBody ReviewCreateRequest dto) {Long id = reviewService.createReview(dto, dto.getUserId());
		return ResponseEntity.ok(id);
	}


	//리뷰 수정 PATCH: /api/v1/reviews/{reviewId}
	@PatchMapping("/{reviewId}")
	public ResponseEntity<Void> update(
		@PathVariable Long reviewId, @RequestBody ReviewUpdateRequest dto) {reviewService.updateReview(reviewId, dto);
		return ResponseEntity.noContent().build();
	}

	//리뷰 삭제 DELETE: /api/v1/reviews/{reviewId}
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Void> delete(@PathVariable Long reviewId) {reviewService.deleteReview(reviewId);
		return ResponseEntity.noContent().build();
	}

	//나의 리뷰 조회 GET: /api/v1/users/me/reviews
	@GetMapping("/me")
	public ResponseEntity<List<Review>> getMyReviews(@RequestParam Long userId) {List<Review> list = reviewService.getMyReviews(userId);
		return ResponseEntity.ok(list);


		// 전체 리뷰 조회 (관리자용)
		//@GetMapping

		// // 단건 상세 조회(마이페이지에서 하나씩 상세 내역 볼때)
		// @GetMapping("/{reviewId}")

	}
}
