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
	@ApiModelProperty(notes = "댓글의 야이디", required = true)
	private Long commentId;

	@ApiModelProperty(notes = "댓글을 작성한 회원의 닉네임", required = true, example = "열정")
	private String nickname;

	@ApiModelProperty(notes = "댓글이 생성된 시각", required = true)
	private LocalDateTime commentCreatedAt;

	@ApiModelProperty(notes = "댓글의 내용", required = true, example = "댓글내용")
	private String content;

	private MyCommentDto(
			Long commentId,
			String nickname,
			LocalDateTime commentCreatedAt,
			String content
	) {
		this.commentId = commentId;
		this.nickname = nickname;
		this.commentCreatedAt = commentCreatedAt;
		this.content = content;
	}

	public static MyCommentDto of(Comment comment) {
		return new MyCommentDto(
				comment.getId(),
				comment.getWriter().getNickname(),
				comment.getCreatedAt(),
				comment.getContent()
		);
	}
}
