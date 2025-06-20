package com.ssginc8.docto.fcm.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
@Log4j2
public class FirebaseCloudMessageServiceImpl implements  FirebaseCloudMessageService{

	private final FcmTokenProvider fcmTokenProvider;
	private final UserProvider userProvider;

	/**
	 * 단순 제목/내용으로 FCM 메세지 전송
	 * @param userId 수신자 ID
	 * @param title 메시지 제목
	 * @param body 메시지 본문
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendMessage(Long userId, String title, String body) {
		Message message = buildMessage(userId, Map.of("title", title, "body", body));
		send(message, userId);
	}

	/**
	 * 클라이언트에서 활용할 추가 데이터를 포함하여 FCM 메시지를 전송합니다.
	 * (복용 알림 등에서 사용)
	 * @param userId 수신자 ID
	 * @param title 메시지 제목
	 * @param body 메시지 본문
	 * @param data 데이터 맵
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendMessageWithData(Long userId, String title, String body, Map<String, String> data) {
		log.info("sendMessageWithData 시작: userId={}", userId);
		// 데이터 맵에 title, body 추가하여 일관성 유지
		data.put("title", title);
		data.put("body", body);

		Message message = buildMessage(userId, data);
		log.info("메시지 빌드 완료, send 호출 전: userId={}, message={}", userId, message);
		send(message, userId);
		log.info("sendMessageWithData 종료: userId={}", userId);
	}

	private Message buildMessage(Long userId, Map<String, String> data) {
		String userFirebaseToken = fcmTokenProvider.findLatestTokenByUserId(userId)
			.map(FcmToken::getToken)
			.filter(token -> token != null && !token.isBlank())
			.orElseThrow(NotFoundTokenException::new);

		log.info("FCM 토큰 조회 성공: userId={}, token={}", userId, userFirebaseToken);

		return Message.builder()
			.putAllData(data)
			.setToken(userFirebaseToken)
			.build();
	}

	private void send(Message message, Long userId) {
		try {
			log.info("전송할 FCM 메세지: {}", message);
			String response = FirebaseMessaging.getInstance().send(message);
			log.info("FCM 메세지 전송 성공: {}", response);

		} catch (FirebaseMessagingException e) {
			log.error("FCM 전송 실패: userId={}, message={}, error={}",userId, message, e.getMessage());
			throw new FailedSendMessage();
		} catch (Exception e) {
			log.error("send 메서드에서 예상치 못한 오류 발생: userId={}, message={}, errorType={}, errorMessage={}",
				userId, message, e.getClass().getName(), e.getMessage(), e);
			throw new RuntimeException("FCM 전송 중 예상치 못한 오류 발생", e);
		}
		log.info("send 메서드 종료: userId={}", userId);
	}

	@Override
	@Transactional
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
