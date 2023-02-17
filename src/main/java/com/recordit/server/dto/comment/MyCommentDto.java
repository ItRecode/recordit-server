package com.recordit.server.dto.comment;

import java.time.LocalDateTime;

import com.recordit.server.domain.Comment;

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
public class MyCommentDto {
	@ApiModelProperty(notes = "레코드의 카테고리 이름")
	private String categoryName;

	@ApiModelProperty(notes = "레코드 아이디")
	private Long recordId;

	@ApiModelProperty(notes = "레코드의 제목")
	private String title;

	@ApiModelProperty(notes = "레코드의 아이콘 이름")
	private String iconName;

	@ApiModelProperty(notes = "레코드의 색상 이름")
	private String colorName;

	@ApiModelProperty(notes = "레코드 작성 시각")
	private LocalDateTime recordCreatedAt;

	@ApiModelProperty(notes = "레코드에 달린 댓글 내용")
	private String commentContent;

	@ApiModelProperty(notes = "레코드에 달린 댓글 작성 시간")
	private LocalDateTime commentCreatedAt;

	@Builder
	public MyCommentDto(Comment comment) {
		this.categoryName = comment.getRecord().getRecordCategory().getName();
		this.recordId = comment.getRecord().getId();
		this.title = comment.getRecord().getTitle();
		this.iconName = comment.getRecord().getRecordIcon().getName();
		this.colorName = comment.getRecord().getRecordColor().getName();
		this.recordCreatedAt = comment.getRecord().getCreatedAt();
		this.commentContent = comment.getContent();
		this.commentCreatedAt = comment.getCreatedAt();
	}
}
