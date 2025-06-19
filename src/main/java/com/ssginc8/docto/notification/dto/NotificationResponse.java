package com.ssginc8.docto.notification.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.text.DateFormatter;

import com.ssginc8.docto.notification.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationResponse {

	private Long notificationId;
	private String content;
	private String type;
	private Long referenceId;
	private boolean isRead;
	private String createdAt;
	private LocalDateTime readAt;

	public static NotificationResponse from(Notification notification) {
		return new NotificationResponse(
			notification.getNotificationId(),
			notification.getContent(),
			notification.getType().getDescription(),
			notification.getReferenceId(),
			notification.isRead(),
			formatKoreanDate(notification.getCreatedAt()),
			notification.getReadAt()
		);
	}

	private static String formatKoreanDate(LocalDateTime createdAt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
		return createdAt.format(formatter);
	}
}
