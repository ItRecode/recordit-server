package com.recordit.server.dto.record;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordByDateRequestDto {

	@ApiParam(value = "조회할 날짜", required = true, example = "2022-11-16")
	@NotNull
	@NotBlank
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@ApiParam(value = "조회할 페이지 번호 !주의: 0이상이어야 합니다.", required = true, example = "0")
	@NotNull
	@Min(0)
	private Integer page;

	@ApiParam(value = "조회할 레코드 갯수 !주의: 1이상이어야 합니다.", required = true, example = "1")
	@NotNull
	@Min(1)
	private Integer size;

}
