package com.recordit.server.exception.member;

public class NotMatchLoginTypeException extends RuntimeException {
	public NotMatchLoginTypeException(String message) {
		super(message);
	}
}
