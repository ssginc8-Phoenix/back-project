package com.ssginc8.docto.review.service;

import static com.ssginc8.docto.appointment.entity.QAppointment.*;
import static com.ssginc8.docto.doctor.entity.QDoctor.*;
import static com.ssginc8.docto.hospital.entity.QHospital.*;
import static com.ssginc8.docto.user.entity.QUser.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.doctor.provider.DoctorProvider;
import com.ssginc8.docto.hospital.provider.HospitalProvider;
import com.ssginc8.docto.review.dto.ReviewAllListResponse;
import com.ssginc8.docto.review.dto.ReviewCreateRequest;
import com.ssginc8.docto.review.dto.ReviewMyListResponse;
import com.ssginc8.docto.review.dto.ReviewResponse;
import com.ssginc8.docto.review.dto.ReviewUpdateRequest;
import com.ssginc8.docto.review.entity.KeywordType;
import com.ssginc8.docto.review.entity.Review;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.doctor.entity.Doctor;

import com.ssginc8.docto.review.provider.ReviewProvider;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewProvider reviewProvider;

	private final UserProvider userProvider;
	private final DoctorProvider doctorProvider;
	private final HospitalProvider hospitalProvider;
	private final AppointmentProvider appointmentProvider;


	// 리뷰 생성
	@Override
	@Transactional
	public Long createReview(ReviewCreateRequest request, Long userId) {

		// 1. 필요한 정보들을 가져온다
		User        user        = userProvider.getUserById(request.getUserId());
		Appointment appointment = appointmentProvider.getAppointmentById(request.getAppointmentId());
		Hospital    hospital    = hospitalProvider.getHospitalById(request.getHospitalId());
		Doctor      doctor      = doctorProvider.getDoctorById(request.getDoctorId());


		//2. 클라이언트가 보낸 키워드 문자열 목록을 처리한다
		Set<KeywordType> keywordTypes = request.getKeywords().stream()
			//2-1. 각 문자열을 열거형으로 바꿔준다
			.map(KeywordType::valueOf)
			//2-2. 변환된 객체들을 중복없이 모아서 Set<KeywordType>으로 만들어준다
			.collect(Collectors.toSet());

		//3. 리뷰에 필요한 객체득들을 모아서 새 리뷰를 만든다
		Review review = Review.create(
			user,
			hospital,
			doctor,
			appointment,
			request.getContents(),
			keywordTypes
		);

		Review saved = reviewProvider.save(review);
		return saved.getReviewId();

	}

	// 리뷰 수정
	@Override
	@Transactional
	public ReviewResponse updateReview(ReviewUpdateRequest request, Long reviewId) {
		// 1. 리뷰를 불러온다
		Review review = reviewProvider.getById(reviewId);

		// 2. 본문 내용을 변경한다
		review.updateContents(request.getContents());

		// 3. 키워드를 변경한다
		Set<KeywordType> keywordTypes = request.getKeywords().stream()
			.map(KeywordType::valueOf)
			.collect(Collectors.toSet());
		review.getKeywords().clear();
		review.getKeywords().addAll(keywordTypes);


		// 4. 응답DTO를 만들어준다
		List<String> keywords = review.getKeywords().stream()
			.map(Enum::name)
			.toList();

		return ReviewResponse.fromEntity(review, keywords);
	}




	// 리뷰 삭제
	@Override
	@Transactional
	public void deleteReview(Long reviewId) {
		// 1. 키워드 먼저 삭제
		reviewProvider.deleteByReviewId(reviewId);
		// 2. 리뷰 본문 삭제
		reviewProvider.deleteById(reviewId);
	}


	//내가 쓴 리뷰 조회
	@Override
	@Transactional
	public Page<ReviewMyListResponse> getMyReviews(Long userId, Pageable pageable) {
		return reviewProvider.getMyReviews(userId, pageable)
			.map(ReviewMyListResponse::fromEntity);
	}

	// 병원별 전체 리뷰 조회
	@Override
	@Transactional(readOnly = true)
	public Page<ReviewAllListResponse> getAllReviews(Long hospitalId, Pageable pageable) {
		return reviewProvider.getHospitalReviews(hospitalId, pageable)
			.map(ReviewAllListResponse::fromEntity);

	}

	//리뷰 신고 횟수 추가 기능
	@Override
	@Transactional
	public void reportReview(Long reviewId) {
		Review review = reviewProvider.getById(reviewId);
		review.incrementReportCount();
		reviewProvider.save(review);
	}


}








