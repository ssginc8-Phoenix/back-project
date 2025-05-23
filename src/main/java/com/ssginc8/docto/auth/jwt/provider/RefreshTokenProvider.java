package com.ssginc8.docto.auth.jwt.provider;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.auth.jwt.entity.RefreshToken;
import com.ssginc8.docto.auth.jwt.repository.RefreshTokenRepo;
import com.ssginc8.docto.global.error.exception.tokenException.InvalidRefreshTokenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Component
@Log4j2
public class RefreshTokenProvider {
	private final RefreshTokenRepo refreshTokenRepo;

	public RefreshToken findByUuid(String uuid) {
		return refreshTokenRepo.findByUuid(uuid).orElse(null);
	}

	public RefreshToken findByRefreshToken(String refreshToken) {
		return refreshTokenRepo.findByRefreshToken(refreshToken)
			.orElseThrow(InvalidRefreshTokenException::new);
	}

	public void saveRefreshToken(RefreshToken refreshToken) {
		refreshTokenRepo.save(refreshToken);
	}

	public void deleteRefreshToken(String refreshToken) {
		refreshTokenRepo.deleteRefreshTokenByRefreshToken(refreshToken);
	}
}