package com.recordit.server.exception.member;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.recordit.server.controller.MemberController;
import com.recordit.server.exception.ErrorMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackageClasses = MemberController.class)
public class MemberExceptionHandler {
	private static String getSimpleName(Exception e) {
		return e.getClass().getSimpleName();
	}

	@ExceptionHandler(NotFoundUserInfoInSessionException.class)
	public ResponseEntity<ErrorMessage> handleNotFoundUserInfoInSessionException(NotFoundUserInfoInSessionException e) {
		return ResponseEntity.badRequest()
				.body(
						ErrorMessage.builder()
								.code(HttpStatus.BAD_REQUEST.value())
								.msg(e.getLocalizedMessage())
								.errorSimpleName(getSimpleName(e))
								.timestamp(LocalDateTime.now())
								.build()
				);
	}

	@ExceptionHandler(NotMatchLoginTypeException.class)
	public ResponseEntity<ErrorMessage> handleNotMatchLoginTypeException(NotMatchLoginTypeException e) {
		return ResponseEntity.badRequest()
				.body(
						ErrorMessage.builder()
								.code(HttpStatus.BAD_REQUEST.value())
								.msg(e.getLocalizedMessage())
								.errorSimpleName(getSimpleName(e))
								.timestamp(LocalDateTime.now())
								.build()
				);
	}

	@ExceptionHandler(NotEnteredLoginTypeException.class)
	public ResponseEntity<ErrorMessage> handleNotEnteredLoginTypeException(NotEnteredLoginTypeException e) {
		return ResponseEntity.badRequest()
				.body(
						ErrorMessage.builder()
								.code(HttpStatus.BAD_REQUEST.value())
								.msg(e.getLocalizedMessage())
								.errorSimpleName(getSimpleName(e))
								.timestamp(LocalDateTime.now())
								.build()
				);
	}
}

