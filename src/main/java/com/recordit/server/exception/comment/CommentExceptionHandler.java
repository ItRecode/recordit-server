package com.recordit.server.exception.comment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.recordit.server.controller.CommentController;
import com.recordit.server.exception.ErrorMessage;
import com.recordit.server.exception.member.MemberNotFoundException;
import com.recordit.server.exception.record.RecordNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackageClasses = CommentController.class)
public class CommentExceptionHandler {

	@ExceptionHandler(EmptyContentException.class)
	public ResponseEntity<ErrorMessage> handleEmptyContentException(EmptyContentException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleRecordNotFoundException(RecordNotFoundException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(CommentNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleCommentNotFoundException(CommentNotFoundException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(MemberNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleMemberNotFoundException(MemberNotFoundException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.INTERNAL_SERVER_ERROR));
	}
}
