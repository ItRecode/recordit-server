package com.recordit.server.dto.record;

import java.util.List;

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
	@ApiModelProperty(notes = "추억 레코드 아이디")
	private Long recordId;

	@ApiModelProperty(notes = "추억 레코드 제목")
	private String title;

	@ApiModelProperty(notes = "추억 레코드 아이콘 이름")
	private String iconName;

	@ApiModelProperty(notes = "추억 레코드 아이콘 색상")
	private String iconColor;

	@ApiModelProperty(notes = "추억 레코드 댓글 리스트 ")
	private List<MemoryRecordCommentDto> commentList;

	@Builder
	public MemoryRecordDto(
			Long recordId,
			String title,
			String iconName,
			String iconColor,
			List<MemoryRecordCommentDto> commentList
	) {
		this.recordId = recordId;
		this.title = title;
		this.iconName = iconName;
		this.iconColor = iconColor;
		this.commentList = commentList;
	}
}
