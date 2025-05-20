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
import org.springframework.security.web.SecurityFilterChain;

import com.ssginc8.docto.auth.service.UserDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final UserDetailService userDetailService;

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
