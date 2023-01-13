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
public class CommentDto {

	@ApiModelProperty(notes = "댓글 ID", required = true)
	private Long commentId;

	@ApiModelProperty(notes = "댓글 작성자", required = true)
	private String writer;

	@ApiModelProperty(notes = "댓글 내용", required = true)
	private String content;

	@ApiModelProperty(notes = "첨부 이미지 URL", required = true)
	private String imageUrl;

	@ApiModelProperty(notes = "하위 댓글 개수", required = false)
	private Long numOfSubComment;

	@ApiModelProperty(notes = "생성 일자", required = true)
	private LocalDateTime createdAt;

	@ApiModelProperty(notes = "수정 일자", required = false)
	private LocalDateTime modifiedAt;

	@Builder
	public CommentDto(Comment comment, String imageUrl, Long numOfSubComment) {
		this.commentId = comment.getId();
		this.writer = (comment.getWriter() != null) ? comment.getWriter().getNickname() : null;
		this.content = comment.getContent();
		this.imageUrl = imageUrl;
		this.numOfSubComment = numOfSubComment;
		this.createdAt = comment.getCreatedAt();
		this.modifiedAt = comment.getModifiedAt();
	}
}
