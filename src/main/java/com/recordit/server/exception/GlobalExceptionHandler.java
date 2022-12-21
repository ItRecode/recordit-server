package com.recordit.server.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static String getSimpleName(Exception e) {
		return e.getClass().getSimpleName();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NotFoundUserInfoInSessionException.class)
	public ErrorMessage handleNotFoundUserInfoInSessionException(NotFoundUserInfoInSessionException e) {
		return ErrorMessage.builder()
				.msg(e.getLocalizedMessage())
				.errorCode(getSimpleName(e))
				.timestamp(LocalDateTime.now())
				.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorMessage handleIllegalArgumentException(IllegalArgumentException e) {
		return ErrorMessage.builder()
				.msg(e.getLocalizedMessage())
				.errorCode(getSimpleName(e))
				.timestamp(LocalDateTime.now())
				.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NullPointerException.class)
	public ErrorMessage handleNullPointerException(NullPointerException e) {
		return ErrorMessage.builder()
				.msg(e.getLocalizedMessage())
				.errorCode(getSimpleName(e))
				.timestamp(LocalDateTime.now())
				.build();
	}
}
