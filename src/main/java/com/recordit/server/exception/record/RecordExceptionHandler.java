package com.recordit.server.exception.record;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.recordit.server.controller.RecordController;
import com.recordit.server.exception.ErrorMessage;
import com.recordit.server.exception.member.MemberNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackageClasses = RecordController.class)
public class RecordExceptionHandler {
	@ExceptionHandler(MemberNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleMemberNotFoundException(
			MemberNotFoundException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(RecordColorNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleRecordColorNotFoundException(
			RecordColorNotFoundException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleRecordNotFoundException(
			RecordNotFoundException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}
}

