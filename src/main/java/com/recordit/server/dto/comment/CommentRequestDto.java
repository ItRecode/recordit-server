package com.recordit.server.dto.comment;

import javax.validation.constraints.NotNull;

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
public class CommentRequestDto {
	@ApiModelProperty(notes = "레코드의 id", required = true)
	@NotNull
	private Long recordId;

	@ApiModelProperty(notes = "자식 댓글을 조회하는 경우 부모 댓글의 id")
	private Long parentId;

	@ApiModelProperty(notes = "댓글 리스트의 현재 페이지", required = true)
	private int page;

	@ApiModelProperty(notes = "댓글 리스트의 사이즈", required = true)
	private int size;
}
