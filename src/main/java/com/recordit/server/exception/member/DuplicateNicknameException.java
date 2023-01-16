package com.recordit.server.exception.member;

public class DuplicateNicknameException extends RuntimeException {
	public DuplicateNicknameException(String message) {
		super(message);
	}
}
