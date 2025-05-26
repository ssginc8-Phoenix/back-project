package com.ssginc8.docto.review.repository;

import java.util.List;
import java.util.Optional;

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


}

