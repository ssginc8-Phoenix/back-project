package com.ssginc8.docto.review.provider;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.review.entity.Review;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.review.repository.ReviewRepo;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

//“조회(Read)” 기능만 모아둔 곳
@Component
@RequiredArgsConstructor
public class ReviewProvider {

	private final ReviewRepo reviewRepo;

	//특정 예약에 달린 리뷰 조회
	@Transactional(readOnly=true)
	public List<Review> getByAppointment(Appointment appointment) {
		return reviewRepo.findByAppointment(appointment);
	}



	//단건 조회
	@Transactional(readOnly = true)
	public Review getById(Long reviewId) {
		return reviewRepo.findById(reviewId)
			.orElseThrow(() -> new IllegalArgumentException(
				"리뷰가 존재하지 않습니다. id=" + reviewId));

	}


	//병원관리자가 본인 병원 리뷰 전체 조회
	@Transactional(readOnly = true)
	public List<Review> getByHospital(Long hospitalId) {
		return reviewRepo.findByHospitalHospitalId(hospitalId);
	}


	//마이페이지에서 본인이 작성한 리뷰 조회(내림차순)
	@Transactional(readOnly = true)
	public List<Review> getMyReviewsOrdered(Long userId) {
		return reviewRepo.findByAuthorUserIdOrderByCreatedAtDesc(userId);
	}



}
