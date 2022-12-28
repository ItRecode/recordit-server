package com.recordit.server.exception.file;

public class EmptyFileException extends RuntimeException {
	public EmptyFileException(String message) {
		super(message);
	}
}
