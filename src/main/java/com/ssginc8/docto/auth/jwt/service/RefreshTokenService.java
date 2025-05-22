package com.ssginc8.docto.auth.jwt.service;

import com.ssginc8.docto.auth.jwt.dto.CreateAccessToken;

public interface RefreshTokenService {
	CreateAccessToken.Response createNewAccessToken(String refreshToken);
}
