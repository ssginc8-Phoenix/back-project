package com.ssginc8.docto.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.review.entity.Review;
import com.ssginc8.docto.user.entity.User;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

	//연관 엔티티 한 번에 패치해서 단건 조회
	@Override
	@EntityGraph(attributePaths = {
		"appointment",
		"hospital",
		"doctor",
		"author"
	})
	Optional<Review> findById(Long reviewId);


	//기본 전체 페이징 조회
	@Override
	Page<Review> findAll(Pageable pageable);


	// 특정 예약 엔티티에 달린 리뷰 조회
	List<Review> findByAppointment(Appointment appointment);


	//마이페이지에서 본인이 작성한 리뷰 조회(내림차순)
	List<Review> findByAuthorUserIdOrderByCreatedAtDesc(Long userId);


    // //병원관리자가 자기 병원 리뷰만 조회할 때
	// List<Review> findByHospitalHospitalId(Long hospitalId);
}
