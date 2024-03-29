package com.recordit.server.exception.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import com.recordit.server.controller.MemberController;
import com.recordit.server.exception.ErrorMessage;

@RestControllerAdvice(basePackageClasses = MemberController.class)
public class MemberExceptionHandler {

	@ExceptionHandler(NotFoundUserInfoInSessionException.class)
	public ResponseEntity<ErrorMessage> handleNotFoundUserInfoInSessionException(
			NotFoundUserInfoInSessionException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(NotMatchLoginTypeException.class)
	public ResponseEntity<ErrorMessage> handleNotMatchLoginTypeException(NotMatchLoginTypeException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(NotEnteredLoginTypeException.class)
	public ResponseEntity<ErrorMessage> handleNotEnteredLoginTypeException(NotEnteredLoginTypeException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(DuplicateNicknameException.class)
	public ResponseEntity<ErrorMessage> handleDuplicateNicknameException(DuplicateNicknameException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ErrorMessage.of(exception, HttpStatus.CONFLICT));
	}

	@ExceptionHandler(NotFoundRegisterSessionException.class)
	public ResponseEntity<ErrorMessage> handleNotFoundRegisterSessionException(
			NotFoundRegisterSessionException exception) {
		return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED)
				.body(ErrorMessage.of(exception, HttpStatus.PRECONDITION_REQUIRED));
	}

	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<ErrorMessage> handleRestClientException(
			RestClientException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorMessage.of(exception, HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(SignupCooldownException.class)
	public ResponseEntity<ErrorMessage> handleSignupCooldownException(
			SignupCooldownException exception) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(ErrorMessage.of(exception, HttpStatus.FORBIDDEN));
	}
}

