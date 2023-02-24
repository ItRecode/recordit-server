package com.recordit.server.dto.record.memory;

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
public class MemoryRecordCommentDto {
	@ApiModelProperty(notes = "추억레코드 댓글 아이디")
	private Long commentId;

	@ApiModelProperty(notes = "추억레코드 댓글 내용")
	private String content;

	private MemoryRecordCommentDto(
			Long commentId,
			String content
	) {
		this.commentId = commentId;
		this.content = content;
	}

	public static MemoryRecordCommentDto of(
			Comment comment
	) {
		return new MemoryRecordCommentDto(
				comment.getId(),
				comment.getContent()
		);
	}

}
