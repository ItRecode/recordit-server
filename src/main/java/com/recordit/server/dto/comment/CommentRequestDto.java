package com.recordit.server.dto.comment;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequestDto {

	@ApiParam(value = "레코드 ID", required = true, example = "0")
	@NotNull
	private Long recordId;

	@ApiParam(value = "자식 댓글을 조회하는 경우 부모 댓글의 ID", example = "0")
	private Long parentId;

	@ApiParam(value = "댓글 리스트의 요청 페이지 !주의: 0부터 시작", required = true, example = "0")
	@NotNull
	private Integer page;

	@ApiParam(value = "댓글 리스트의 사이즈", required = true, example = "10")
	@NotNull
	private Integer size;
}
