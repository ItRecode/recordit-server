package com.recordit.server.dto.record.category;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.recordit.server.domain.RecordCategory;

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

	@ApiModelProperty(notes = "레코드 카테고리 ID", required = true)
	private Long id;

	@ApiModelProperty(notes = "레코드 카테고리 이름", required = true)
	private String name;

	@ApiModelProperty(notes = "하위 레코드 목록", required = true)
	private List<RecordCategoryResponseDto> subcategories;

	@Builder
	public RecordCategoryResponseDto(Long id, String name, List<RecordCategory> subcategories) {
		this.id = id;
		this.name = name;
		this.subcategories = subcategories.stream().map(
				subcategory -> RecordCategoryResponseDto.builder()
						.id(subcategory.getId())
						.name(subcategory.getName())
						.subcategories(subcategory.getSubcategories())
						.build()
		).collect(Collectors.toList());
	}

}
