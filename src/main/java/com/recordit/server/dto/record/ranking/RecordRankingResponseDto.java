package com.recordit.server.dto.record.ranking;

import java.util.List;
import java.util.stream.Collectors;

import com.recordit.server.domain.Record;

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

	public static RecordRankingResponseDto of(List<Record> records) {
		List<RecordRankingDto> mapToRecordRankingDtos = records.stream()
				.map(RecordRankingDto::of)
				.collect(Collectors.toList());
		return new RecordRankingResponseDto(mapToRecordRankingDtos);
	}
}
