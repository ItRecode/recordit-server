package com.recordit.server.constant;

import java.util.Arrays;

import com.recordit.server.exception.record.NotMatchRankingPeriodException;

public enum RankingPeriod {
	DAY,
	WEEK,
	MONTH,
	TOTAL;

	public static RankingPeriod findByString(String str) {
		return Arrays.stream(RankingPeriod.values())
				.filter(rankingPeriod -> rankingPeriod.name().equals(str))
				.findFirst()
				.orElseThrow(() -> new NotMatchRankingPeriodException("일치하는 랭킹 집계 기간이 없습니다."));
	}
}
