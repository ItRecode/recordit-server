package com.recordit.server.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorMessage {
	private String msg;
	private String errorCode;
	private LocalDateTime timestamp;
}