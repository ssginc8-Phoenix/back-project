package com.ssginc8.docto.review.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewProvider reviewProvider;

	@PersistenceContext
	private EntityManager em;

	// 리뷰 생성
	@Override
	@Transactional
	public Long createReview(ReviewCreateRequest request, Long userId) {
		//1. 필요한 프록시를 가져온다
		User user = em.getReference(User.class, userId);
		Appointment appointment = em.getReference(Appointment.class, request.getAppointmentId());
		Hospital    hospital    = em.getReference(Hospital.class,    request.getHospitalId());
		Doctor      doctor      = em.getReference(Doctor.class,      request.getDoctorId());


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

	// //리뷰 수정
	// @Override
	// @Transactional
	// public ReviewResponse updateReview(ReviewUpdateRequest request, Long reviewId) {
	// 	//1. 리뷰를 불러온다
	// 	Review review = reviewProvider.getById(reviewId);
	//
	// 	//2. 본문 내용을 변경한다
	// 	review.updateContents(request.getContents());
	//
	// 	//3. 키워드를 변경한다
	// 	Set<KeywordType> keywordTypes = request.getKeywords().stream()
	// 		.map(KeywordType::valueOf)
	// 		.collect(Collectors.toSet());
	// 	review.getKeywords().clear();
	// 	review.getKeywords().addAll(keywordTypes);
	//
	// 	//4. 변경된 값들을 저장한다
	// 	Review saved = reviewProvider.save(review);
	//
	// 	//5. 응답DTO를 만들어준다
	// 	List<String> keywords = saved.getKeywords().stream()
	// 		.map(Enum::name)
	// 		.toList();
	//
	// 	return ReviewResponse.fromEntity(saved, keywords);
	//
	// }

	@Override
	@Transactional
	public ReviewResponse updateReview(ReviewUpdateRequest req, Long reviewId) {
		Review review = reviewProvider.getById(reviewId);
		review.updateContents(req.getContents());

		Set<KeywordType> kws = req.getKeywords().stream()
			.map(KeywordType::valueOf)
			.collect(Collectors.toSet());
		review.getKeywords().clear();
		review.getKeywords().addAll(kws);

		// ★ 이미 영속 상태이므로 save() 생략해도 됩니다.
		List<String> keywords = review.getKeywords().stream()
			.map(Enum::name).toList();
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








