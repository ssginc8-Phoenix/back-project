package com.ssginc8.docto.auth.jwt.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssginc8.docto.auth.jwt.dto.CreateAccessToken;
import com.ssginc8.docto.auth.jwt.dto.TokenType;
import com.ssginc8.docto.auth.jwt.provider.TokenProvider;
import com.ssginc8.docto.auth.jwt.service.RefreshTokenServiceImpl;
import com.ssginc8.docto.util.CookieUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private final TokenProvider tokenProvider;
	private final RefreshTokenServiceImpl refreshTokenServiceImpl;
	private final CookieUtil cookieUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		log.info("----------------------필터 동작-----------------------");
		String accessToken = getToken(request, TokenType.ACCESS_TOKEN.getTokenType());

		if (tokenProvider.validToken(accessToken)) { // 토큰 검증 -> 토큰이 유효하다면
			// 토큰 기반으로 인증 정보 가져오기
			Authentication authentication = tokenProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else { // 유효하지 않다면
			String refreshToken = getToken(request, TokenType.REFRESH_TOKEN.getTokenType());
			if (tokenProvider.validToken(refreshToken)) {
				CreateAccessToken.Response createAccessToken = refreshTokenServiceImpl.createNewAccessToken(
					refreshToken);
				accessToken = createAccessToken.getAccessToken();

				response.addCookie(
					cookieUtil.createCookie(TokenType.ACCESS_TOKEN.getTokenType(), createAccessToken.getAccessToken(),
						createAccessToken.getAccessTokenCookieMaxAge()));

				Authentication authentication = tokenProvider.getAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request, String tokenType) {
		String token = null;

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(tokenType)) {
					token = cookie.getValue();
					break;
				}
			}
		}

		return token;
	}
}