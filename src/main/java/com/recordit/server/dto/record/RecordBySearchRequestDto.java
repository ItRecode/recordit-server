package com.recordit.server.dto.record;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordBySearchRequestDto {
	@ApiParam(value = "조회할 검색어", required = true, example = "레코드 제목")
	@NotNull
	@Pattern(regexp = "^.{1,12}$", message = "검색어 파라미터는 1글자 이상 12글자 이하입니다.")
	private String searchKeyword;

	@ApiParam(value = "조회할 페이지 번호 !주의: 0이상이어야 합니다.", required = true, example = "0")
	@NotNull
	@Min(0)
	private Integer page;

	@ApiParam(value = "조회할 레코드 갯수 !주의: 1이상이어야 합니다.", required = true, example = "1")
	@NotNull
	@Min(1)
	private Integer size;
}
