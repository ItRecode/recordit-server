package com.recordit.server.dto.comment;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@ApiModel
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WriteCommentResponseDto {

	private Long commentId;

	@Builder
	public WriteCommentResponseDto(Long commentId) {
		this.commentId = commentId;
	}
}
