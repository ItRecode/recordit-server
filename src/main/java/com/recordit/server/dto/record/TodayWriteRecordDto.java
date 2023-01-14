package com.recordit.server.dto.record;

import java.time.LocalDateTime;

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
public class TodayWriteRecordDto {
	@ApiModelProperty(notes = "레코드 작성 시각")
	private LocalDateTime createdAt;

	@ApiModelProperty(notes = "레코드의 카테고리 이름")
	private String categoryName;

	@ApiModelProperty(notes = "레코드의 제목")
	private String title;

	@ApiModelProperty(notes = "레코드에 달린 댓글 갯수")
	private Long commentCount;

	@ApiModelProperty(notes = "레코드의 아이콘 이름")
	private String iconName;

	@ApiModelProperty(notes = "레코드의 색상 이름")
	private String colorName;

	@Builder
	public TodayWriteRecordDto(
			LocalDateTime createdAt,
			String categoryName,
			String title,
			Long commentCount,
			String iconName,
			String colorName
	) {
		this.createdAt = createdAt;
		this.categoryName = categoryName;
		this.title = title;
		this.commentCount = commentCount;
		this.iconName = iconName;
		this.colorName = colorName;
	}
}