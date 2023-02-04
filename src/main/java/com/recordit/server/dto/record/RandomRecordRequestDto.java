package com.recordit.server.dto.record;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@ApiModel
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RandomRecordRequestDto {
	@ApiParam(value = "카테고리 ID", required = true, example = "1")
	@NotNull
	private Long recordCategoryId;

	@ApiParam(value = "댓글 리스트의 사이즈", required = true, example = "5")
	@NotNull
	private Integer size;
}
