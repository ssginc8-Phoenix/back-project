package com.ssginc8.docto.fcm.service;

import java.util.Map;

import com.ssginc8.docto.fcm.dto.FcmMessageRequest;
import com.ssginc8.docto.user.entity.User;

/**
 *  Firebase Admin SDK를 사용하여 실제로 Firebase 서버에 푸시 알림을 보내는 역할
 */
public interface FirebaseCloudMessageService {

	void sendMessage(Long userId, String title, String body);

	void sendMessageWithData(Long userId, String title, String body, Map<String, String> data);

	void saveToken(Long userId, String token);
}
