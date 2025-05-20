package com.ssginc8.docto.qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.qna.entity.QaPost;
import com.ssginc8.docto.qna.provider.QaPostProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QaPostServiceImpl implements QaPostService{

	private final QaPostProvider qaPostProvider;

	@Transactional
	@Override
	public QaPost createQaPost(Appointment appointment, String content) {
		QaPost post = QaPost.create(appointment, content);
		return qaPostProvider.save(post);
	}
}
