package com.recordit.server.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.recordit.server.exception.ErrorMessage;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class FileExceptionHandler {

	@ExceptionHandler(FileInputStreamException.class)
	public ResponseEntity<ErrorMessage> handleFileInputStreamException(FileInputStreamException exception) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ErrorMessage.of(exception, HttpStatus.UNPROCESSABLE_ENTITY));
	}
}
