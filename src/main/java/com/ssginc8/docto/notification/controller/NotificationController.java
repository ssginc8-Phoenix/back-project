package com.ssginc8.docto.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.notification.dto.NotificationResponse;
import com.ssginc8.docto.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	/**
	 * 알림 읽음
	 * @param notificationId
	 * @return
	 */
	@PatchMapping("/{notificationId}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
		notificationService.markAsRead(notificationId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 로그인한 유저의 알림 목록 조회
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<NotificationResponse>> getUserNotifications() {
		return ResponseEntity.ok(notificationService.getNotificationsByLoginUser());
	}
}
