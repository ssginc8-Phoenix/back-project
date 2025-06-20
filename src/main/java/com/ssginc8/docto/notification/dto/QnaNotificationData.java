package com.ssginc8.docto.notification.dto;

import java.time.LocalDateTime;

import com.ssginc8.docto.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class QnaNotificationData {

	private User receiver;
	private String hospitalName;
	private String patientName;
	private LocalDateTime postTime;
}
