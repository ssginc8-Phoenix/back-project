package com.ssginc8.docto.qna.service;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.qna.dto.QaPostResponse;
import com.ssginc8.docto.qna.entity.QaPost;

public interface QaPostService {

	//예약에 연결된 새 게시글 생성
	QaPost createQaPost(Appointment appointment, String qaPost);

	// 기존 게시글 수정
	QaPostResponse updateQaPost(Long qnaId, String content);

	// 게시글 단건 조회
	QaPostResponse getQaPost(Long qnaId);

	// Q&A 게시글 삭제
	void deleteQaPost(Long qnaId);
}
