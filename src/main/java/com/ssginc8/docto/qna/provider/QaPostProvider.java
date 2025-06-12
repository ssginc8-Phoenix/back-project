package com.ssginc8.docto.qna.provider;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.global.error.exception.qnaException.QnaNotFoundException;
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
			.orElseThrow(QnaNotFoundException::new);
	}

	// 게시글 저장
	@Transactional
	public QaPost save(QaPost qaPost) {
		return qaPostRepo.save(qaPost);
	}

	@Transactional(readOnly = true)
	public Optional<QaPost> findByAppointment(Appointment appointment) {
		return qaPostRepo.findByAppointment(appointment);
	}

	@Transactional(readOnly = true)
	public Page<QaPost> findAllByUserId(Long userId, Pageable pageable) {
		// Appointment→patientGuardian→user→userId 경로에 맞춘 repo 메서드 호출
		return qaPostRepo.findAllByAppointment_PatientGuardian_User_UserId(userId, pageable);
	}


}
