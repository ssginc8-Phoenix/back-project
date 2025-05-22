package com.ssginc8.docto.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ssginc8.docto.auth.handler.OAuth2SuccessHandler;
import com.ssginc8.docto.auth.jwt.filter.TokenAuthenticationFilter;
import com.ssginc8.docto.auth.jwt.provider.TokenProvider;
import com.ssginc8.docto.auth.jwt.service.RefreshTokenServiceImpl;
import com.ssginc8.docto.auth.service.UserDetailService;
import com.ssginc8.docto.util.CookieUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final TokenProvider tokenProvider;
	private final RefreshTokenServiceImpl refreshTokenServiceImpl;
	private final UserDetailService userDetailService;
	private final CookieUtil cookieUtil;
	private final DefaultOAuth2UserService oAuth2UserService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;

	// 특정 http 요청에 대해 웹 기반 보안 구성
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/static/**", "/api/v1/auth/login/**", "/api/v1/users/register",
					"/api/v1/users/check-email", "/api/v1/users/find-email", "/api/v1/users/password-reset",
					"/api/v1/users/email/**"
				).permitAll()

				// .requestMatchers(
				//
				// ).hasRole("PATIENT")
				//
				// .requestMatchers(
				// 	"/api/v1/patients/**"
				// ).hasRole("GUARDIAN")
				//
				// .requestMatchers(
				//
				// ).hasRole("HOSPITAL_ADMIN")
				//
				// .requestMatchers(
				//
				// ).hasRole("DOCTOR")
				//
				// .requestMatchers(
				//
				// ).hasRole("SYSTEM_ADMIN")

				.anyRequest().authenticated()) // 그외 모든 요청 인증 필요
			.csrf(AbstractHttpConfigurer::disable)
			.oauth2Login(oauth2 -> oauth2
				// "api/v1/auth/login" 뒤에 /kakao or /naver 붙여서 로그인 요청
				.authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/login"))
				.redirectionEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/callback/*"))
				.userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
				.successHandler(oAuth2SuccessHandler) // 로그인 성공시 OAuth2SuccessHandler 동작
			)
			.addFilterBefore(new TokenAuthenticationFilter(tokenProvider, refreshTokenServiceImpl, cookieUtil),
				UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	// 인증 관리자 관련 설정
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http,
		BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService); // 사용자 정보를 가져올 서비스 생성
		authProvider.setPasswordEncoder(bCryptPasswordEncoder); // 비밀번호 암호화하기 위한 인코더 설정
		return new ProviderManager(authProvider);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
