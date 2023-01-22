package com.recordit.server.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {

	public static LocalDateTime getStartOfToday() {
		return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
	}
}
