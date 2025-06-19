package com.ssginc8.docto.fcm.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.fcm.entity.FcmToken;
import com.ssginc8.docto.fcm.provider.FcmTokenProvider;
import com.ssginc8.docto.global.error.exception.fcmException.FailedSendMessage;
import com.ssginc8.docto.global.error.exception.fcmException.NotFoundTokenException;
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
		Optional<FcmToken> optionalFcmToken = fcmTokenProvider.findLatestTokenByUserId(userId);

		if (optionalFcmToken.isEmpty() || optionalFcmToken.get().getToken() == null || optionalFcmToken.get().getToken().isBlank()) {
			log.warn("유효한 FCM 토큰이 없습니다. 알림을 전송할 수 없습니다. userId={}", userId);
			throw new NotFoundTokenException();
		}

		String userFirebaseToken = optionalFcmToken.get().getToken();
		log.info("FCM 토큰 조회 성공: userId={}, token={}", userId, userFirebaseToken);

		// 메시지 구성
		Message message = Message.builder()
			.putData("title", title)
			.putData("body", body)
			.setToken(userFirebaseToken)	// 조회한 토큰 값을 사용
			.build();

		log.info("전송할 FCM 메시지: {}", message);

		try {
			// 메세지 전송
			String response = FirebaseMessaging.getInstance().send(message);
			log.info("FCM 메시지 전송 성공: {}", response);
			return "Message sent successfully: " + response;

		} catch (FirebaseMessagingException e) {
			log.error("FCM 전송 실패: userId={}, error={}", userId, e.getMessage(), e);
			throw new FailedSendMessage();
		}
	}

	@Override
	public void saveToken(Long userId, String token) {
		User user = userProvider.getUserById(userId);

		// 1. 해당 토큰이 이미 다른 유저에게 등록되어 있는 경우 제거
		Optional<FcmToken> existingToken = fcmTokenProvider.findByToken(token);
		if (existingToken.isPresent() && !existingToken.get().getUser().getUserId().equals(userId)) {
			fcmTokenProvider.delete(existingToken.get());
		}

		// 2. 현재 유저에게 동일한 토큰이 이미 등록돼 있지 않다면 저장
		Optional<FcmToken> latestToken = fcmTokenProvider.findLatestTokenByUserId(userId);

		if (latestToken.isEmpty() || !latestToken.get().getToken().equals(token)) {
			FcmToken newFcmToken = new FcmToken(user, token);
			fcmTokenProvider.save(newFcmToken);

		}
	}
}
