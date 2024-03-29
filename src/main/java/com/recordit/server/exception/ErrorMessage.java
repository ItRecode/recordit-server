package com.recordit.server.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {
	private int code;
	private String errorSimpleName;
	private String msg;
	private LocalDateTime timestamp;

	public ErrorMessage(Exception exception, HttpStatus httpStatus, String message) {
		this.code = httpStatus.value();
		this.errorSimpleName = exception.getClass().getSimpleName();
		this.msg = message;
		this.timestamp = LocalDateTime.now();
	}

	public ErrorMessage(Exception exception, HttpStatus httpStatus) {
		this.code = httpStatus.value();
		this.errorSimpleName = exception.getClass().getSimpleName();
		this.msg = exception.getMessage();
		this.timestamp = LocalDateTime.now();
	}

	public static ErrorMessage of(Exception exception, HttpStatus httpStatus) {
		return new ErrorMessage(exception, httpStatus);
	}

	public static ErrorMessage of(Exception exception, HttpStatus httpStatus, String message) {
		return new ErrorMessage(exception, httpStatus, message);
	}
}