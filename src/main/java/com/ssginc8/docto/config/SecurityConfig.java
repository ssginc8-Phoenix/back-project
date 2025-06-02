package com.ssginc8.docto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// test security config
@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable()) // ✅ CSRF 보호 비활성화
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/v1/guardians/**").permitAll()
				.requestMatchers("/api/v1/**").permitAll()
				.anyRequest().authenticated()
			);
		return http.build();
	}
}

