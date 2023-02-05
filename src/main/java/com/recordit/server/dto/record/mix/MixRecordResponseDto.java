package com.recordit.server.dto.record.mix;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@ApiModel
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MixRecordResponseDto {
	@ApiModelProperty(notes = "믹스레코드 리스트")
	private List<MixRecordDto> mixRecordDto;

	@Builder
	public MixRecordResponseDto(List<MixRecordDto> mixRecordDto) {
		this.mixRecordDto = mixRecordDto;
	}
}
