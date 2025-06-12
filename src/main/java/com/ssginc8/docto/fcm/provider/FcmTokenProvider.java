package com.ssginc8.docto.fcm.provider;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.fcm.entity.FcmToken;
import com.ssginc8.docto.fcm.repo.FcmTokenRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FcmTokenProvider {

	private final FcmTokenRepo fcmTokenRepo;

	public Optional<FcmToken> findLatestTokenByUserId(Long userId) {
		return fcmTokenRepo.findLatestTokenByUserId(userId);
	}

	public Optional<FcmToken> findByToken(String token) {
		return fcmTokenRepo.findByToken(token);
	}

	public FcmToken save(FcmToken fcmToken) {
		return fcmTokenRepo.save(fcmToken);
	}
}
