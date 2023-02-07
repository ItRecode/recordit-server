package com.recordit.server.dto.record.category;

import java.util.List;
import java.util.stream.Collectors;

import com.recordit.server.domain.RecordCategory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@ApiModel
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordCategoryResponseDto {

	@ApiModelProperty(notes = "레코드 카테고리 ID", required = true)
	private Long id;

	@ApiModelProperty(notes = "레코드 카테고리 이름", required = true)
	private String name;

	private RecordCategoryResponseDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static RecordCategoryResponseDto of(RecordCategory recordCategory) {
		return new RecordCategoryResponseDto(recordCategory.getId(), recordCategory.getName());
	}

	public static List<RecordCategoryResponseDto> of(List<RecordCategory> recordCategories) {
		return recordCategories.stream().map(RecordCategoryResponseDto::of).collect(Collectors.toList());
	}
}
