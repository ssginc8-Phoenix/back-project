package com.ssginc8.docto.review.provider;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.global.error.exception.reviewException.ReviewNotFoundException;
import com.ssginc8.docto.review.entity.Review;
import com.ssginc8.docto.review.repository.ReviewRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewProvider {

	private final ReviewRepo reviewRepo;



	// 내가 쓴 리뷰 조회
	@Transactional(readOnly = true)
	public Page<Review> getMyReviews(Long userId, Pageable pageable) {
		return reviewRepo.findByUserUserIdOrderByCreatedAtDesc(userId, pageable);
	}


	// 병원 전체 리뷰 조회
	@Transactional(readOnly = true)
	public Page<Review> getHospitalReviews(Long hospitalId, Pageable pageable) {
		return reviewRepo.findByHospitalHospitalId(hospitalId, pageable);
	}

	// admin 전체 리뷰 조회
	@Transactional(readOnly = true)
	public Page<Review> getAllReviews(Pageable pageable) {
		return reviewRepo.findAll(pageable);
	}



	//리뷰 단건 조회
	@Transactional(readOnly = true)
	public Review getById(Long reviewId) {
		return reviewRepo.findWithGraphByReviewId(reviewId)
			.orElseThrow(ReviewNotFoundException::new);
	}



	//리뷰 저장
	@Transactional
	public Review save(Review review) {
		return reviewRepo.save(review);
	}



	//키워드 삭제
	@Transactional
	public void deleteByReviewId(Long reviewId) {
		Review review = reviewRepo.findWithGraphByReviewId(reviewId)
			.orElseThrow(ReviewNotFoundException::new);
		review.getKeywords().clear();
	}


}
