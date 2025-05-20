package com.ssginc8.docto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// CSRF 비활성화 (Postman 테스트 용)
			.csrf(csrf -> csrf.disable())
			// 모든 요청 허용
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll()
			);
		return http.build();
	}
}
