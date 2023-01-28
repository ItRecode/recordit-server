package com.recordit.server.dto.comment;

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
public class DeleteCommentRequestDto {
	@ApiModelProperty(notes = "레코드의 id", required = true)
	private Long recordId;

	@Builder
	public DeleteCommentRequestDto(Long recordId) {
		this.recordId = recordId;
	}
}
