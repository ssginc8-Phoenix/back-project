package com.ssginc8.docto.notification.entity;

import java.time.LocalDateTime;

import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notificationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", nullable = false)
	private User receiver;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType type;

	@Column(nullable = false)
	private Long referenceId;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private boolean isRead;

	private LocalDateTime readAt;

	public Notification(User receiver, NotificationType type, String content, Long referenceId) {
		this.receiver = receiver;
		this.type = type;
		this.content = content;
		this.referenceId = referenceId;
		this.isRead = false;
		this.readAt = null;
	}

	public void markAsRead() {
		if (!this.isRead) {
			this.isRead = true;
			this.readAt = LocalDateTime.now();
		}
	}
}
