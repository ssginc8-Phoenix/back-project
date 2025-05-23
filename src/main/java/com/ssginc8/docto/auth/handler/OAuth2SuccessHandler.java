package com.ssginc8.docto.auth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ssginc8.docto.auth.entity.CustomOAuth2User;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final UserProvider userProvider;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		String providerId = oAuth2User.getName();

		// providerId를 기반으로 user 찾기
		User user = userProvider.loadUserByProviderId(providerId);

		log.info("소셜 로그인 성공 " + user.getUserId());

		// // user의 role이 비어있다면 환자, 보호자, 병원 관리자인지 선택하는 화면으로 리다이렉트 (user의 providerId 세션으로 같이 넘기기)
		// // 소셜 로그인으로 회원 가입
		// // phone, 프로필 이미지, role 입력 받기 => 이후 role에 맞게 추가 정보 입력 (환자 - 주소, 주민번호) / (병원 관리자 - 의사 정보)
		// if (Objects.isNull(user.getRole())) {
		// 	response.sendRedirect("");
		// } else { // 비어있지 않다면 메인 페이지로 리다이렉트
		// 	response.sendRedirect("");
		// }
	}
}
