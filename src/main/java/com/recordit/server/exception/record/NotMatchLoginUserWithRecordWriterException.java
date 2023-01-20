package com.recordit.server.exception.record;

public class NotMatchLoginUserWithRecordWriterException extends RuntimeException {
	public NotMatchLoginUserWithRecordWriterException(String message) {
		super(message);
	}
}
