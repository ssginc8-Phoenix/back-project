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

	@Transactional(readOnly = true)
	public String getQaPostByAppointment(Appointment appointment) {
		return qaPostRepo.findByAppointment(appointment)
			.map(QaPost::getContent)
			.orElse(null);
	}

	@Transactional
	public QaPost save(QaPost qaPost) {
		return qaPostRepo.save(qaPost);
	}
}
