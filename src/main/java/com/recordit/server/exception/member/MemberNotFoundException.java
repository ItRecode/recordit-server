package com.recordit.server.exception.member;

public class MemberNotFoundException extends RuntimeException {
	public MemberNotFoundException(String message) {
		super(message);
	}
}