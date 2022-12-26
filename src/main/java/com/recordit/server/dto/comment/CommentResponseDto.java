package com.recordit.server.dto.comment;

import java.util.List;

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
public class CommentResponseDto {
	@ApiModelProperty(notes = "레코드의 id", required = true)
	@NotNull
	private Long recordId;

	@ApiModelProperty(notes = "자식 댓글의 리스트일 경우 부모 댓글이 들어감")
	private Long parentId;

	@ApiModelProperty(notes = "레코드 댓글의 전체 개수", required = true)
	private Long totalCount;

	@ApiModelProperty(notes = "댓글 리스트", required = true)
	private List<CommentDto> commentList;
}
