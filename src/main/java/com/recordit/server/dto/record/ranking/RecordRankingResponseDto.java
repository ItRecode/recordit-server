package com.recordit.server.dto.record.ranking;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@ApiModel
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordRankingResponseDto {

	private List<RecordRankingDto> recordRankingDtos;
	private LocalDateTime rankingAggregationTime;

	private RecordRankingResponseDto(List<RecordRankingDto> recordRankingDtos, LocalDateTime rankingAggregationTime) {
		this.recordRankingDtos = recordRankingDtos;
		this.rankingAggregationTime = rankingAggregationTime;
	}

	public static RecordRankingResponseDto of(
			List<RecordRankingDto> recordRankingDtos,
			LocalDateTime rankingAggregationTime
	) {
		return new RecordRankingResponseDto(recordRankingDtos, rankingAggregationTime);
	}
}
