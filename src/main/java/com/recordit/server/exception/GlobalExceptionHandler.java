package com.recordit.server.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException exception) {
		String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();

		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST, message));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorMessage> handleConstraintViolationException(
			ConstraintViolationException exception) {
		String message = exception.getConstraintViolations().iterator().next().getMessage();

		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST, message));
	}
}
