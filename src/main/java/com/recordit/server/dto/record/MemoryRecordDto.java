package com.recordit.server.dto.record;

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
public class MemoryRecordDto {
	@ApiModelProperty(notes = "마이 레코드 아이디")
	private Long recordId;

	@ApiModelProperty(notes = "마이 레코드 제목")
	private String title;

	@ApiModelProperty(notes = "마이 레코드 아이콘 이름")
	private String iconName;

	@Builder
	public MemoryRecordDto(Long recordId, String title, String iconName) {
		this.recordId = recordId;
		this.title = title;
		this.iconName = iconName;
	}
}
