package com.recordit.server.dto.record.category;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecordCategoryResponseDto {
	@ApiModelProperty(notes = "레코드 카테고리 목록", required = true)
	private Map<String, List<String>> recordCategory;

	@Builder
	public RecordCategoryResponseDto(Map<String, List<String>> recordCategory) {
		this.recordCategory = recordCategory;
	}
}
