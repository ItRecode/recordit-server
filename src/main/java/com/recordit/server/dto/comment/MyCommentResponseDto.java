package com.recordit.server.dto.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.recordit.server.domain.Comment;

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
public class MyCommentResponseDto {
	@ApiModelProperty(notes = "내가 작성한 댓글의 전체 페이지 개수", required = true)
	private Integer totalPage;

	@ApiModelProperty(notes = "내가 작성한 댓글의 전체 개수", required = true)
	private Long totalCount;

	@ApiModelProperty(notes = "내가 작성한 댓글 리스트", required = true)
	private List<MyCommentDto> myCommentDtos;

	@Builder
	public MyCommentResponseDto(Page<Comment> comments) {
		totalPage = comments.getTotalPages();
		totalCount = comments.getTotalElements();
		myCommentDtos = comments.getContent().stream()
				.map(
						comment -> MyCommentDto.builder().comment(comment).build()
				).collect(Collectors.toList());
	}
}
