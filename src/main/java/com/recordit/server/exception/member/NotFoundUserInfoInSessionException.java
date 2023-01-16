package com.recordit.server.exception.member;

public class NotFoundUserInfoInSessionException extends RuntimeException {
	public NotFoundUserInfoInSessionException(String message) {
		super(message);
	}
}
