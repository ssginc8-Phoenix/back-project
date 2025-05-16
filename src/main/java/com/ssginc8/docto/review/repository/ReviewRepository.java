package com.ssginc8.docto.review.repository;

import com.ssginc8.docto.review.entities.Review;
import com.ssginc8.docto.review.entities.KeywordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	// ── 환자·사용자 ─────────────────────────────────────────────────

	// 지난 예약에 대해 이미 작성된 리뷰가 있는지 체크
	boolean existsByAppointment_AppointmentIdAndAuthor_UserId(Long appointmentId, Long userId);


	//내가 작성한 모든 리뷰 조회
	List<Review> findByAuthor_UserId(Long userId);


	//내가 작성한 특정 리뷰 조회 (수정 전 검증)
	Optional<Review> findByReviewIdAndAuthor_UserId(Long reviewId, Long userId);


	// //내가 작성한 리뷰 삭제(이건 없어도 될거 같아요)
	// void deleteByReviewIdAndAuthor_UserId(Long reviewId, Long userId);



	// ── 의사·병원 관리자 ───────────────────────────────────────────────


	//특정 의사에게 달린 리뷰 페이징 조회
	Page<Review> findByDoctor_DoctorId(Long doctorId, Pageable pageable);


	//특정 의사의 특정 리뷰 조회
	Optional<Review> findByReviewIdAndDoctor_DoctorId(Long reviewId, Long doctorId);


	// 리뷰 신고 시 reportCount를 +1
	@Modifying
	@Query("UPDATE Review r SET r.reportCount = r.reportCount + 1 WHERE r.reviewId = :reviewId")
	void incrementReportCount(@Param("reviewId") Long reviewId);


	//전체 리뷰에서 특정 키워드 등장 횟수 집계
	@Query("""
         SELECT COUNT(rk)
         FROM ReviewKeyword rk
         WHERE rk.keyword = :keyword """)

	long countByKeyword(@Param("keyword") KeywordType keyword);
}
