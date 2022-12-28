package com.recordit.server.exception.record;

public class RecordNotFoundException extends RuntimeException {
	public RecordNotFoundException(String message) {
		super(message);
	}
}
