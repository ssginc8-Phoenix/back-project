package com.ssginc8.docto.qna.service;

import com.ssginc8.docto.qna.dto.QaPostCreateRequest;
import com.ssginc8.docto.qna.dto.QaPostResponse;

public interface QaPostService {

	//예약에 연결된 새 게시글 생성
	QaPostResponse createQaPost(QaPostCreateRequest request);

	// 기존 게시글 수정
	QaPostResponse updateQaPost(Long qnaId, String content);

	// 게시글 단건 조회
	QaPostResponse getQaPost(Long qnaId);

	// Q&A 게시글 삭제
	void deleteQaPost(Long qnaId);
}
