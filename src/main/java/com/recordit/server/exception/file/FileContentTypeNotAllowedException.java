package com.recordit.server.exception.file;

public class FileContentTypeNotAllowedException extends RuntimeException {
	public FileContentTypeNotAllowedException(String message) {
		super(message);
	}
}
