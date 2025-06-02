package com.ssginc8.docto.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.auth.jwt.dto.TokenType;
import com.ssginc8.docto.auth.jwt.provider.RefreshTokenProvider;
import com.ssginc8.docto.util.CookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {
	private final RefreshTokenProvider refreshTokenProvider;
	private final CookieUtil cookieUtil;

	@Transactional
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String refreshToken = cookieUtil.getToken(request, TokenType.REFRESH_TOKEN.getTokenType());

		refreshTokenProvider.deleteRefreshToken(refreshToken);

		Cookie accessTokenCookie = cookieUtil.createCookie(TokenType.ACCESS_TOKEN.getTokenType(), null, 0);
		response.addCookie(accessTokenCookie);

		Cookie refreshTokenCookie = cookieUtil.createCookie(TokenType.REFRESH_TOKEN.getTokenType(), null, 0);
		response.addCookie(refreshTokenCookie);
	}
}
