package com.recordit.server.exception.record;

public class NotMatchLoginUserWithRecordWriter extends RuntimeException {
	public NotMatchLoginUserWithRecordWriter(String message) {
		super(message);
	}
}
