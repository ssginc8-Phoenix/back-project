package com.ssginc8.docto.notification.dto;

import java.time.LocalDateTime;

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
	private LocalDateTime createdAt;
	private LocalDateTime readAt;

	public static NotificationResponse from(Notification notification) {
		return new NotificationResponse(
			notification.getNotificationId(),
			notification.getContent(),
			notification.getType().name(),
			notification.getReferenceId(),
			notification.isRead(),
			notification.getCreatedAt(),
			notification.getReadAt()
		);
	}
}
