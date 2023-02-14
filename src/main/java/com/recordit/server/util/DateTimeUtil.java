package com.recordit.server.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

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

	public static LocalDateTime getFirstDayOfMonth(YearMonth yearMonth) {
		LocalDate standardDate = getStandardDate(yearMonth);
		return getStartOfDay(standardDate.withDayOfMonth(1));
	}

	public static LocalDateTime getLastDayOfMonth(YearMonth yearMonth) {
		LocalDate standardDate = getStandardDate(yearMonth);
		return getEndOfDay(standardDate.withDayOfMonth(standardDate.lengthOfMonth()));
	}

	private static LocalDate getStandardDate(YearMonth yearMonth) {
		return LocalDate.of(
				yearMonth.getYear(),
				yearMonth.getMonth(),
				1
		);
	}
}
