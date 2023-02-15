package com.recordit.server.dto.record.ranking;

import javax.validation.constraints.NotNull;

import com.recordit.server.constant.RankingPeriod;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@ApiModel
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RecordRankingRequestDto {

	@ApiParam(value = "조회 할 레코드 카테고리 ID", required = true)
	@NotNull(message = "레코드 카테고리 ID를 지정해야 합니다")
	private Long recordCategoryId;

	@ApiParam(value = "조회 할 랭킹의 집계 범위", required = true)
	@NotNull(message = "랭킹 집계 범위를 지정해야 합니다")
	private RankingPeriod rankingPeriod;

}
