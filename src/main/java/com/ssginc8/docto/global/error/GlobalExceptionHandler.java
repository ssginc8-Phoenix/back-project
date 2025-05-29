package com.ssginc8.docto.global.error;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssginc8.docto.global.error.exception.BusinessBaseException;

import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
		return createErrorResponseEntity(ErrorCode.UNAUTHORIZED_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
		return createErrorResponseEntity(ErrorCode.FORBIDDEN_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
		e.printStackTrace();
		return createErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e) {
		e.printStackTrace();
		return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(BusinessBaseException.class)
	protected ResponseEntity<ErrorResponse> handle(BusinessBaseException e) {
		e.printStackTrace();
		return createErrorResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handle(Exception e) {
		e.printStackTrace();
		return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
		ResponseEntity<ErrorResponse> responseResponseEntity = new ResponseEntity<>(ErrorResponse.of(errorCode),
			errorCode.getStatus());
		log.info("=============================================");
		log.info(responseResponseEntity.getBody());
		return responseResponseEntity;
	}
}
