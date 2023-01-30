package com.recordit.server.exception.comment;

public class NotAllowedModifyWhenNonMemberException extends RuntimeException {
	public NotAllowedModifyWhenNonMemberException(String message) {
		super(message);
	}
}
