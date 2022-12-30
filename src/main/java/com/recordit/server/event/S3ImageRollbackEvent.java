package com.recordit.server.event;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class S3ImageRollbackEvent {
	private final String rollbackFileName;

	private S3ImageRollbackEvent(String rollbackFileName) {
		this.rollbackFileName = rollbackFileName;
	}

	public static S3ImageRollbackEvent from(String rollbackFileName) {
		return new S3ImageRollbackEvent(rollbackFileName);
	}
}
