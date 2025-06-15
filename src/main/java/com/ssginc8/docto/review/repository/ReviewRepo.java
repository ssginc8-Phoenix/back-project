package com.ssginc8.docto.review.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssginc8.docto.review.entity.Review;



@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {


	Page<Review> findByUserUserIdOrderByCreatedAtDesc(Long UserId, Pageable pageable);

	Page<Review> findByHospitalHospitalId(Long hospitalId, Pageable pageable);

	@EntityGraph(attributePaths = {
		"user",
		"hospital",
		"doctor",
		"appointment",
		"keywords"
	})
	Optional<Review> findWithGraphByReviewId(Long reviewId);

	Page<Review> findByUserUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(
		Long userId,
		Pageable pageable
	);

	Optional<Review> findByReviewId(Long reviewId);

  	/**
	 * appointment Id로 review 가져오기
	 */
	@Query("""
		SELECT r.appointment.appointmentId
		FROM Review r
		WHERE r.appointment.appointmentId IN :appointmentIds
	""")
	Set<Long> findAppointmentIdsWithReview(@Param("appointmentIds") List<Long> appointmentIds);
}

