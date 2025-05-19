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
import com.ssginc8.docto.review.provider.ReviewProvider;
import com.ssginc8.docto.review.repository.ReviewRepo;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepo reviewRepo;
	private final ReviewProvider reviewProvider;

	@PersistenceContext
	private EntityManager em;

	@Override
	public Long createReview(ReviewCreateRequest req, Long userId) {
		//연관 엔티티
		Appointment appointment = em.getReference(Appointment.class, req.getAppointmentId());
		Doctor doctor = em.getReference(Doctor.class, req.getDoctorId());
		Hospital hospital = em.getReference(Hospital.class, req.getHospitalId());
		User author = em.getReference(User.class, userId);

		// 도메인 팩토리 메서드로 Review 생성
		Review review = Review.create(
			appointment,
			doctor,
			hospital,
			author,
			req.getContents(),
			req.getKeywords()
		);

		// 저장 및 ID 반환
		return reviewRepo.save(review).getReviewId();
	}

	@Override
	public void updateReview(Long reviewId, ReviewUpdateRequest req) {
		// 단건 조회
		Review review = reviewProvider.getById(reviewId);
		// 본문수정
		review.updateContent = (req.getContents());
		// review.setKeywords(req.getKeywords());
	}

	// @Override
	// public void updateKeywords(Long reviewId, List<KeywordType> keywords) {
	// 	Review review = reviewProvider.getById(reviewId);
	// 	review.setKeywords(keywords);
	// }

	@Override
	public void deleteReview(Long reviewId) {
		Review review = reviewProvider.getById(reviewId);
		reviewRepo.delete(review);
	}

    @Override
	public List<Review> getMyReviews(Long userId) {
		// ReviewProvider에서 구현한 “내 리뷰 최신순 조회” 메서드를 그대로 호출
		return reviewProvider.getMyReviewsOrdered(userId);
	}

}
