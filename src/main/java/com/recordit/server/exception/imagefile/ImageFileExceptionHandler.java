package com.recordit.server.exception.imagefile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.recordit.server.exception.ErrorMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ImageFileExceptionHandler {

	@ExceptionHandler(FileInputStreamException.class)
	public ResponseEntity<ErrorMessage> handleFileInputStreamException(FileInputStreamException exception) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ErrorMessage.of(exception, HttpStatus.UNPROCESSABLE_ENTITY));
	}
}
