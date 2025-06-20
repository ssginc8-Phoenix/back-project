package com.ssginc8.docto.global.event.qna;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ssginc8.docto.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class QnaAnsweredEventListener {

	private final NotificationService notificationService;

	@Async
	@TransactionalEventListener
	public void handleQnaAnsweredEvent(QnaAnsweredEvent event) {
		log.info("Q&A 답변 알림 이벤트 발생 !!!");

		notificationService.notifyQnaResponse(
			event.getQnaPostId(),
			event.getAnsweredAt(),
			event.getQnaCommentId()
		);
	}
}
