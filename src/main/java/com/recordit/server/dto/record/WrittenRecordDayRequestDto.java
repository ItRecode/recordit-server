package com.recordit.server.dto.record;

import java.time.YearMonth;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
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
public class WrittenRecordDayRequestDto {

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM")
	private YearMonth yearMonth;
}

