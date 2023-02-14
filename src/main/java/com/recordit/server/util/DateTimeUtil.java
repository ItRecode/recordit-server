package com.recordit.server.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {

	public static LocalDateTime getStartOfToday() {
		return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
	}

	public static LocalDateTime getStartOfDay(LocalDate localDate) {
		return LocalDateTime.of(localDate, LocalTime.MIN);
	}

	public static LocalDateTime getEndOfDay(LocalDate localDate) {
		return LocalDateTime.of(localDate, LocalTime.MAX);
	}

	public static LocalDateTime getFirstDayOfMonth(LocalDateTime dateTime) {
		return getStartOfDay(dateTime.withDayOfMonth(1).toLocalDate());
	}

	public static LocalDateTime getLastDayOfMonth(LocalDateTime dateTime) {
		return getEndOfDay(dateTime.withDayOfMonth(dateTime.toLocalDate().lengthOfMonth()).toLocalDate());
	}
}
