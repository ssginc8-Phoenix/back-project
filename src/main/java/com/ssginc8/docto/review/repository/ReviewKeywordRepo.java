package com.ssginc8.docto.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssginc8.docto.review.entity.Review;
import com.ssginc8.docto.review.entity.ReviewKeyword;

@Repository
public interface ReviewKeywordRepo extends JpaRepository<ReviewKeyword, Long> {

	// 특정 리뷰에 속한 키워드 일괄 삭제
	void deleteByReview(Review review);

	// 리뷰 키워드 조회
	List<ReviewKeyword> findByReview(Review review);

}
