package com.recordit.server.dto.record;

import java.util.Set;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WrittenRecordDayResponseDto {
	@ApiParam(value = "내가 쓴 레코드 일자 목록", required = true)
	private Set<Integer> WrittenRecordDayDto;

	private WrittenRecordDayResponseDto(Set<Integer> WrittenRecordDayDto) {
		this.WrittenRecordDayDto = WrittenRecordDayDto;
	}

	public static WrittenRecordDayResponseDto of(Set<Integer> WrittenRecordDayDto) {
		return new WrittenRecordDayResponseDto(WrittenRecordDayDto);
	}
}
