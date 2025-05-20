package com.ssginc8.docto.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.review.dto.ReviewCreateRequest;
import com.ssginc8.docto.review.dto.ReviewUpdateRequest;
import com.ssginc8.docto.review.entity.Review;
import com.ssginc8.docto.review.entity.ReviewKeyword;
import com.ssginc8.docto.review.provider.ReviewProvider;
import com.ssginc8.docto.review.repository.ReviewKeywordRepo;
import com.ssginc8.docto.review.repository.ReviewRepo;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepo         reviewRepo;
	private final ReviewKeywordRepo  reviewKeywordRepo;
	private final ReviewProvider     reviewProvider;

	@PersistenceContext
	private EntityManager em;

	//리뷰 생성
	@Override
	@Transactional
	public Long createReview(ReviewCreateRequest req, Long userId) {
		// 연관 엔티티 프록시 가져오기
		Appointment appointment = em.getReference(Appointment.class, req.getAppointmentId());
		Doctor      doctor      = em.getReference(Doctor.class,      req.getDoctorId());
		Hospital    hospital    = em.getReference(Hospital.class,    req.getHospitalId());
		User        author      = em.getReference(User.class,        userId);

		// 리뷰 생성 및 저장 (한 번만 할당)
		Review review = Review.create(appointment, doctor, hospital, author, req.getContents());
		Review savedReview = reviewRepo.save(review);

		// 키워드 저장
		req.getKeywords().forEach(kt -> {
			ReviewKeyword rk = ReviewKeyword.builder()
				.review(savedReview)
				.keywords(kt)
				.build();
			reviewKeywordRepo.save(rk);
		});

		return savedReview.getReviewId();
	}

	//리뷰 수정
	@Override
	@Transactional
	public void updateReview(Long reviewId, ReviewUpdateRequest req) {
		// 리뷰 조회
		Review review = reviewProvider.getById(reviewId);


		// 기존 키워드 삭제 후 재등록
		reviewKeywordRepo.deleteByReview(review);
		req.getKeywords().forEach(kt -> {
			ReviewKeyword rk = ReviewKeyword.builder()
				.review(review)
				.keywords(kt)
				.build();
			reviewKeywordRepo.save(rk);
		});
	}

	//리뷰 삭제
	@Transactional
	@Override
	public void deleteReview(Long reviewId) {
		Review review = reviewProvider.getById(reviewId);
		reviewKeywordRepo.deleteByReview(review);
		reviewRepo.delete(review);
	}

	//리뷰 조회
	@Override
	@Transactional(readOnly = true)
	public List<Review> getMyReviews(Long userId) {
		return reviewProvider.getMyReviewsOrdered(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Review> getAllReviews() {
		return reviewProvider.getAllReviews();
	}

}
