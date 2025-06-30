package com.ssginc8.docto.auth.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final HandlerExceptionResolver resolver;

	public CustomAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void commence(@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull AuthenticationException authException) {

		resolver.resolveException(request, response, null, authException);
	}
}