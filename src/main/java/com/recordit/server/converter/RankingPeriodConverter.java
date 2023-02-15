package com.recordit.server.converter;

import org.springframework.core.convert.converter.Converter;

import com.recordit.server.constant.RankingPeriod;

public class RankingPeriodConverter implements Converter<String, RankingPeriod> {

	@Override
	public RankingPeriod convert(String source) {
		return RankingPeriod.findByString(source);
	}
}
