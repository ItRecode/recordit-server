package com.recordit.server.dto.record.memory;

import java.time.LocalDate;

import javax.validation.constraints.Min;
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
public class MemoryRecordRequestDto {

	@ApiParam(value = "조회할 날짜", example = "2022-11-16")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@ApiParam(value = "추억 레코드 조회 페이지 !주의: 0부터 시작", required = true, example = "0")
	@NotNull
	@Min(0)
	private Integer memoryRecordPage;

	@ApiParam(value = "페이지당 조회할 레코드 사이즈", required = true, example = "7")
	@NotNull
	@Min(1)
	private Integer memoryRecordSize;

	@ApiParam(value = "레코드당 조회할 댓글 갯수", required = true, example = "5")
	@NotNull
	@Min(1)
	private Integer sizeOfCommentPerRecord;
}
