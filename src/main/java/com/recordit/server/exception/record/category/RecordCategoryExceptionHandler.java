package com.recordit.server.exception.record.category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.recordit.server.controller.RecordCategoryController;
import com.recordit.server.exception.ErrorMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackageClasses = RecordCategoryController.class)
public class RecordCategoryExceptionHandler {
	@ExceptionHandler(RecordCategoryNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleRecordCategoryNotFoundException(
			RecordCategoryNotFoundException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(HaveParentRecordCategoryException.class)
	public ResponseEntity<ErrorMessage> handleHaveParentRecordCategoryException(
			HaveParentRecordCategoryException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorMessage.of(exception, HttpStatus.BAD_REQUEST));
	}
}
