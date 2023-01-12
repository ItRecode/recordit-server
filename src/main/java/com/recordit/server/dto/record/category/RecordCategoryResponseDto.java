package com.recordit.server.dto.record.category;

import java.util.ArrayList;
import java.util.List;

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
public class RecordCategoryResponseDto {

	@ApiModelProperty(notes = "레코드 카테고리 ID", required = true)
	private Long id;

	@ApiModelProperty(notes = "레코드 카테고리 이름", required = true)
	private String name;

	@ApiModelProperty(notes = "하위 레코드 목록", required = true)
	private List<RecordCategoryResponseDto> subcategories = new ArrayList<>();

	@Builder
	public RecordCategoryResponseDto(RecordCategory recordCategory, List<RecordCategoryResponseDto> children) {
		this.id = recordCategory.getId();
		this.name = recordCategory.getName();
		this.subcategories = children;
	}
}
