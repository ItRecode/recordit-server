package com.recordit.server.dto.record.mix;

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
public class MixRecordDto {
	@ApiModelProperty(notes = "믹스 레코드 아이디")
	private Long recordId;

	@ApiModelProperty(notes = "믹스 레코드 색상이름")
	private String colorName;

	@ApiModelProperty(notes = "믹스 레코드 아이콘이름")
	private String iconName;

	@ApiModelProperty(notes = "믹스 레코드 댓글 아이디")
	private Long commentId;

	@ApiModelProperty(notes = "믹스 레코드 댓글 내용")
	private String commentContent;

	@Builder
	public MixRecordDto(
			Long recordId,
			String colorName,
			String iconName,
			Long commentId,
			String commentContent
	) {
		this.recordId = recordId;
		this.colorName = colorName;
		this.iconName = iconName;
		this.commentId = commentId;
		this.commentContent = commentContent;
	}
}
