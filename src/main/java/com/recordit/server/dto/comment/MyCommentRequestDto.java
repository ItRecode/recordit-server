package com.recordit.server.dto.comment;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MyCommentRequestDto {
	@ApiParam(value = "조회할 페이지 번호 !주의: 0이상이어야 합니다.", required = true, example = "0")
	@NotNull
	@Min(0)
	private Integer page;

	@ApiParam(value = "조회할 레코드 갯수 !주의: 1이상이어야 합니다.", required = true, example = "1")
	@NotNull
	@Min(1)
	private Integer size;
}
