package com.ssginc8.docto.qna.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssginc8.docto.qna.dto.QaPostCreateRequest;
import com.ssginc8.docto.qna.dto.QaPostResponse;
import com.ssginc8.docto.qna.entity.QaStatus;

public interface QaPostService {

	// 게시글 생성
	QaPostResponse createQaPost(QaPostCreateRequest request);

	// 기존 게시글 수정
	QaPostResponse updateQaPost(Long qnaId, String content);

	// 게시글 단건 조회
	QaPostResponse getQaPost(Long qnaId);

	// Q&A 게시글 삭제
	void deleteQaPost(Long qnaId);

	//예약별 Q&A 조회
	QaPostResponse getByAppointment(Long appointmentId);

	//내가 작성한 Q&A 목록(페이징)
	Page<QaPostResponse> getMyPosts(Long userId, Pageable pageable);


	Page<QaPostResponse> getDoctorPostsByStatus(QaStatus status, Pageable pageable);

	QaPostResponse updateQaPostStatus(Long qnaId, QaStatus status);

}







