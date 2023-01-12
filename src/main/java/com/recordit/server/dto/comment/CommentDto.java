package com.recordit.server.dto.comment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
public class CommentDto {

	@ApiModelProperty(notes = "댓글 ID", required = true)
	private Long commentId;

	@ApiModelProperty(notes = "댓글 작성자", required = true)
	private String writer;

	@ApiModelProperty(notes = "댓글 내용", required = true)
	private String content;

	@ApiModelProperty(notes = "첨부 이미지 URL", required = true)
	private String imageUrl;

	@ApiModelProperty(notes = "생성 일자")
	private LocalDateTime createdAt;

	@ApiModelProperty(notes = "수정 일자")
	private LocalDateTime modifiedAt;

	@Builder
	public CommentDto(Comment comment, String imageUrl) {
		this.commentId = comment.getId();
		this.writer = (comment.getWriter() != null) ? comment.getWriter().getNickname() : null;
		this.content = comment.getContent();
		this.imageUrl = imageUrl;
		this.createdAt = comment.getCreatedAt();
		this.modifiedAt = comment.getModifiedAt();
	}
}
