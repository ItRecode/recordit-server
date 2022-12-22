package com.recordit.server.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorMessage {
	private int code;
	private String errorSimpleName;
	private String msg;
	private LocalDateTime timestamp;
}