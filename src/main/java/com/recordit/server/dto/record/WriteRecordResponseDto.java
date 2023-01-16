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
public class WriteRecordResponseDto {
	private Long recordId;

	@Builder
	public WriteRecordResponseDto(Long recordId) {
		this.recordId = recordId;
	}
}
