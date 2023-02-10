package com.recordit.server.dto.record;

import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

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
public class RecentRecordRequestDto {

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime dateTime;

	@ApiParam(value = "페이지 번호", required = true, example = "0")
	@NotNull
	@Min(0)
	private Integer page;

	@ApiParam(value = "최신 레코드 갯수", required = true, example = "1")
	@NotNull
	@Min(1)
	private Integer size;
}
