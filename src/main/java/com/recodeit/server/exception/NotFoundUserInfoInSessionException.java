package com.recodeit.server.exception;

public class NotFoundUserInfoInSessionException extends RuntimeException {
	public NotFoundUserInfoInSessionException(String message) {
		super(message);
	}
}
