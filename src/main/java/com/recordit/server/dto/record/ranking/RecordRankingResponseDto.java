package com.recordit.server.dto.record.ranking;

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

	private RecordRankingResponseDto(List<RecordRankingDto> recordRankingDtos) {
		this.recordRankingDtos = recordRankingDtos;
	}

	public static RecordRankingResponseDto of(List<RecordRankingDto> recordRankingDtos) {
		return new RecordRankingResponseDto(recordRankingDtos);
	}
}
