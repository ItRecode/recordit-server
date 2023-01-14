package com.recordit.server.dto.record;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@ApiModel
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayWriteRecordResponseDto {
	private TodayWriteRecordDto todayWriteRecord;

	@Builder
	public TodayWriteRecordResponseDto(TodayWriteRecordDto todayWriteRecordDto) {
		this.todayWriteRecord = todayWriteRecordDto;
	}
}
