package com.recordit.server.dto.record.category;

import javax.validation.constraints.NotBlank;

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
public class SaveRecordCategoryRequestDto {
	@ApiModelProperty(notes = "부모 카테고리 ID", required = true)
	private Long parentCategoryId;

	@ApiModelProperty(notes = "카테고리 이름", required = true)
	@NotBlank(message = "카테고리 이름은 빈 값일 수 없습니다.")
	private String name;

	@Builder
	public SaveRecordCategoryRequestDto(Long parentCategoryId, String name) {
		this.parentCategoryId = parentCategoryId;
		this.name = name;
	}
}
