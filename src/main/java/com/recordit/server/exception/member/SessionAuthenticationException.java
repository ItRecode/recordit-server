package com.recordit.server.exception.member;

public class SessionAuthenticationException extends RuntimeException {
	public SessionAuthenticationException(String message) {
		super(message);
	}
}
