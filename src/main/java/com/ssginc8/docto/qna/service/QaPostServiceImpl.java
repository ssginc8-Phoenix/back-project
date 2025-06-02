package com.ssginc8.docto.qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.AppointmentStatus;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.qna.dto.QaPostCreateRequest;
import com.ssginc8.docto.qna.dto.QaPostResponse;
import com.ssginc8.docto.qna.entity.QaPost;
import com.ssginc8.docto.qna.provider.QaPostProvider;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QaPostServiceImpl implements QaPostService{

	private final QaPostProvider qaPostProvider;
	private final AppointmentProvider appointmentProvider;


	// 게시글 생성
	@Transactional
	@Override
	public QaPostResponse createQaPost(QaPostCreateRequest request) {
		// appointment 조회
		Appointment appointment = appointmentProvider.getAppointmentById(request.getAppointmentId());
		QaPost post = QaPost.create(appointment, request.getContent());

		QaPost saved = qaPostProvider.save(post);
		// DTO로 반환
		return QaPostResponse.fromEntity(saved);
	}


	// 게시글 수정
	@Override
	@Transactional
	public QaPostResponse updateQaPost(Long qnaId, String content) {
		// 1. 기존 게시글 조회
		QaPost existing = qaPostProvider.getById(qnaId);

		// 2. 연관된 Appointment에서 상태 조회
		AppointmentStatus status = existing.getAppointment().getStatus();

		// 3. 상태가 REQUESTED 또는 CONFIRMED일 때만 수정
		if (status == AppointmentStatus.REQUESTED || status == AppointmentStatus.CONFIRMED)
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
		QaPost qaPost = qaPostProvider.getById(qnaId);
		qaPost.delete();
		qaPostProvider.save(qaPost);
	}
}
