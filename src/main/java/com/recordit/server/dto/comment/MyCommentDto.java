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
	@ApiModelProperty(notes = "댓글을 작성한 회원의 닉네임")
	private String nickname;

	@ApiModelProperty(notes = "댓글이 생성된 시각")
	private LocalDateTime commentCreatedAt;

	@ApiModelProperty(notes = "댓글의 내용")
	private String content;

	private MyCommentDto(
			String nickname,
			LocalDateTime commentCreatedAt,
			String content
	) {
		this.nickname = nickname;
		this.commentCreatedAt = commentCreatedAt;
		this.content = content;
	}

	public static MyCommentDto of(Comment comment) {
		return new MyCommentDto(
				comment.getWriter().getNickname(),
				comment.getCreatedAt(),
				comment.getContent()
		);
	}
}
