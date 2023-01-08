package com.recordit.server.dto.comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WriteCommentRequestDto {

	@ApiModelProperty(notes = "레코드의 id", required = true)
	@NotNull(message = "레코드 ID를 지정해야 합니다")
	private Long recordId;

	@ApiModelProperty(notes = "자식 댓글일 경우 부모 댓글의 id")
	private Long parentId;

	@ApiModelProperty(notes = "댓글 내용", required = true)
	@Size(max = 100, message = "댓글 내용은 200자를 넘길 수 없습니다")
	private String comment;

	@Builder
	public WriteCommentRequestDto(Long recordId, Long parentId, String comment) {
		this.recordId = recordId;
		this.parentId = parentId;
		this.comment = comment;
	}
}
