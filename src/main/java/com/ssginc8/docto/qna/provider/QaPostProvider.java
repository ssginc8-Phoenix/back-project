package com.ssginc8.docto.qna.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.qna.entity.QaPost;
import com.ssginc8.docto.qna.repo.QaPostRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QaPostProvider {

	private final QaPostRepo qaPostRepo;

	// 예약에 연결된 게시글의 내용을 반환
	@Transactional(readOnly = true)
	public String getQaPostByAppointment(Appointment appointment) {
		return qaPostRepo.findByAppointment(appointment)
			.map(QaPost::getContent)
			.orElse(null);
	}

	// 게시글 조회
	@Transactional(readOnly = true)
	public QaPost getById(Long qnaId) {
		return qaPostRepo.findById(qnaId)
			.orElseThrow(() ->
				new IllegalArgumentException("Q&A 게시글이 없습니다. id=" + qnaId)
			);
	}

	// 게시글 저장
	@Transactional
	public QaPost save(QaPost qaPost) {
		return qaPostRepo.save(qaPost);
	}


	// 게시글을 삭제
	@Transactional
	public void deleteById(Long qnaId) {
		qaPostRepo.deleteById(qnaId);
	}

}
