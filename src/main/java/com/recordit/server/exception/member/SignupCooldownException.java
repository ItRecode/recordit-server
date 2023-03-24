package com.recordit.server.exception.member;

public class SignupCooldownException extends RuntimeException {
	public SignupCooldownException(String message) {
		super(message);
	}
}
