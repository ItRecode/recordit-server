package com.recordit.server.dto.comment;

import java.util.List;

import javax.validation.constraints.Size;

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
public class ModifyCommentRequestDto {

	@ApiModelProperty(notes = "댓글 내용", required = true)
	@Size(max = 100, message = "댓글 내용은 100자를 넘길 수 없습니다")
	private String comment;

	private List<String> deleteImages;

	@Builder
	public ModifyCommentRequestDto(String comment, List<String> deleteImages) {
		this.comment = comment;
		this.deleteImages = deleteImages;
	}
}
