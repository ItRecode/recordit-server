package com.recordit.server.exception.file;

public class FileExtensionNotAllowedException extends RuntimeException {
	public FileExtensionNotAllowedException(String message) {
		super(message);
	}
}
