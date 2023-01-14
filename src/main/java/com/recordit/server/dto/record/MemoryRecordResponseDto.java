package com.recordit.server.dto.record;

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
public class MemoryRecordResponseDto {
	@ApiModelProperty(notes = "다음 페이지가 있는지 여부")
	private Boolean hasNextPage;

	@ApiModelProperty(notes = "처음 페이지 인지 여부")
	private Boolean isFirstPage;

	@ApiModelProperty(notes = "마지막 페이지 인지 여부")
	private Boolean isLastPage;
	@ApiModelProperty(notes = "추억 레코드 리스트")
	private List<MemoryRecordDto> memoryRecordList;

	@Builder
	public MemoryRecordResponseDto(
			Boolean hasNextPage,
			Boolean isFirstPage,
			Boolean isLastPage,
			List<MemoryRecordDto> myRecordList
	) {
		this.hasNextPage = hasNextPage;
		this.isFirstPage = isFirstPage;
		this.isLastPage = isLastPage;
		this.memoryRecordList = myRecordList;
	}
}
