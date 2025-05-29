package com.ssginc8.docto.qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.qna.dto.QaPostResponse;
import com.ssginc8.docto.qna.entity.QaPost;
import com.ssginc8.docto.qna.provider.QaPostProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QaPostServiceImpl implements QaPostService{

	private final QaPostProvider qaPostProvider;


	// 게시글 생성
	@Transactional
	@Override
	public QaPost createQaPost(Appointment appointment, String content) {
		QaPost post = QaPost.create(appointment, content);
		return qaPostProvider.save(post);
	}

	// 게시글 수정
	@Override
	@Transactional
	public QaPostResponse updateQaPost(Long qnaId, String content) {
		QaPost existing = qaPostProvider.getById(qnaId);
		existing.setContent(content);
		QaPost saved = qaPostProvider.save(existing);
		return QaPostResponse.fromEntity(saved);
	}

	// 게시글 조회
	@Override
	@Transactional(readOnly = true)
	public QaPostResponse getQaPost(Long qnaId) {
		return QaPostResponse.fromEntity(qaPostProvider.getById(qnaId));
	}

	// 게시글 삭제
	@Override
	@Transactional
	public void deleteQaPost(Long qnaId) {
		qaPostProvider.deleteById(qnaId);
	}
}
