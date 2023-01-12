package com.recordit.server.dto.comment;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentRequestDto {

	@ApiModelProperty(notes = "레코드 ID", required = true)
	@NotNull
	private Long recordId;

	@ApiModelProperty(notes = "자식 댓글을 조회하는 경우 부모 댓글의 ID")
	private Long parentId;

	@ApiModelProperty(notes = "댓글 리스트의 요청 페이지 !주의: 0부터 시작", required = true)
	@NotNull
	private int page;

	@ApiModelProperty(notes = "댓글 리스트의 사이즈", required = true)
	@NotNull
	private int size;
}
