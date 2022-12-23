package com.recordit.server.exception.redis;

public class RedisParsingException extends RuntimeException {
	public RedisParsingException(String message) {
		super(message);
	}
}
