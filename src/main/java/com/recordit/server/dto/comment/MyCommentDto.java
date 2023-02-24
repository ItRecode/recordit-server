package com.recordit.server.dto.comment;

import java.time.LocalDateTime;

import com.recordit.server.domain.Comment;

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

	private MyCommentDto(
			String categoryName,
			Long recordId,
			String title,
			String iconName,
			String colorName,
			LocalDateTime recordCreatedAt,
			String commentContent,
			LocalDateTime commentCreatedAt
	) {
		this.categoryName = categoryName;
		this.recordId = recordId;
		this.title = title;
		this.iconName = iconName;
		this.colorName = colorName;
		this.recordCreatedAt = recordCreatedAt;
		this.commentContent = commentContent;
		this.commentCreatedAt = commentCreatedAt;
	}

	public static MyCommentDto of(Comment comment) {
		return new MyCommentDto(
				comment.getRecord().getRecordCategory().getName(),
				comment.getRecord().getId(),
				comment.getRecord().getTitle(),
				comment.getRecord().getRecordIcon().getName(),
				comment.getRecord().getRecordColor().getName(),
				comment.getRecord().getCreatedAt(),
				comment.getContent(),
				comment.getCreatedAt()
		);
	}
}
