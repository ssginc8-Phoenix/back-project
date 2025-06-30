package com.ssginc8.docto.auth.jwt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.auth.jwt.dto.CreateAccessToken;
import com.ssginc8.docto.auth.jwt.provider.RefreshTokenProvider;
import com.ssginc8.docto.auth.jwt.provider.TokenProvider;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
	private final TokenProvider tokenProvider;
	private final RefreshTokenProvider refreshTokenProvider;
	private final UserProvider userProvider;

	@Transactional(readOnly = true)
	@Override
	public CreateAccessToken.Response createNewAccessToken(String refreshToken) {
		String uuid = refreshTokenProvider.findByRefreshToken(refreshToken).getUuid();
		User user = userProvider.loadUserByUuid(uuid);

		return tokenProvider.generateAccessToken(uuid, user.getRole().getKey());
	}
}
