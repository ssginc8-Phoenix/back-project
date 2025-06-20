package com.ssginc8.docto.notification.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.notification.entity.Notification;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

	List<Notification> findAllByReceiver_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);
}
