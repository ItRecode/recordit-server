package com.recordit.server.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.recordit.server.constant.RankingPeriod;

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

	public static LocalDateTime getBeforePeriodFromNow(RankingPeriod rankingPeriod) {
		LocalDateTime now = LocalDateTime.now();
		if (rankingPeriod == RankingPeriod.DAY) {
			return now.minusDays(1);
		} else if (rankingPeriod == RankingPeriod.WEEK) {
			return now.minusWeeks(1);
		} else if (rankingPeriod == RankingPeriod.MONTH) {
			return now.minusMonths(1);
		}
		return LocalDateTime.MIN;
	}
}
