package com.ssginc8.docto.global.event.qna;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ssginc8.docto.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QnaAnsweredEventListener {

	private final NotificationService notificationService;

	@Async
	@TransactionalEventListener
	public void handleQnaAnsweredEvent(QnaAnsweredEvent event) {
		notificationService.notifyQnaResponse(event.getQaComment());
	}
}
