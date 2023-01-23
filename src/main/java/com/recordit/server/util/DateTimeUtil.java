package com.recordit.server.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {

	public static LocalDateTime getStartOfDay(LocalDate localDate) {
		return LocalDateTime.of(localDate, LocalTime.MIN);
	}

	public static LocalDateTime getEndOfDay(LocalDate localDate) {
		return LocalDateTime.of(localDate, LocalTime.MAX);
	}
}
