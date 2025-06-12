package com.ssginc8.docto.fcm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.ssginc8.docto.fcm.dto.FcmMessageRequest;
import com.ssginc8.docto.fcm.entity.FcmToken;
import com.ssginc8.docto.fcm.provider.FcmTokenProvider;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class FirebaseCloudMessageServiceImpl implements  FirebaseCloudMessageService{

	private final FcmTokenProvider fcmTokenProvider;
	private final UserProvider userProvider;

	/**
	 * 메세지 전송
	 * @Body requestDto
	 * @return
	 */
	@Override
	public String sendMessage(Long userId, String title, String body) {
		// 사용자의 Firebase 토큰 값을 조회
		String userFirebaseToken = fcmTokenProvider.findLatestTokenByUserId(userId);

		// 메시지 구성
		Message message = Message.builder()
			.putData("title", title)
			.putData("body", body)
			.setToken(userFirebaseToken)	// 조회한 토큰 값을 사용
			.build();

		try {
			// 메세지 전송
			String response = FirebaseMessaging.getInstance().send(message);
			return "Message sent successfully: " + response;

		} catch (FirebaseMessagingException e) {
			log.warn("FCM 전송 실패: userId={}, error={}", userId, e.getMessage());
			return "Failed to send message: " + e.getMessage();
		}
	}

	@Override
	public void saveToken(User user, String token) {
		FcmToken fcmToken = new FcmToken(user, token);
		fcmTokenProvider.save(fcmToken);
	}

	@Override
	public void registerOrUpdateToken(Long userId, String token) {
		User user = userProvider.getUserById(userId);

		// 이미 존재하는 경우 save만 호출
		fcmTokenProvider.findByToken(token)
			.ifPresentOrElse(
				existingFcmToken -> {},
				() -> saveToken(user, token)
			);
	}
}
