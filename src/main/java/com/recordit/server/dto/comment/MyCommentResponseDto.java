package com.recordit.server.dto.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

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
public class MyCommentResponseDto {
	@ApiModelProperty(notes = "내가 작성한 댓글의 전체 페이지 개수", required = true)
	private Integer totalPage;

	@ApiModelProperty(notes = "내가 작성한 댓글의 전체 개수", required = true)
	private Long totalCount;

	@ApiModelProperty(notes = "내가 작성한 댓글 리스트", required = true)
	private List<MyCommentDto> myCommentDtos;

	private MyCommentResponseDto(
			Integer totalPage,
			Long totalCount,
			List<MyCommentDto> myCommentDtos
	) {
		this.totalPage = totalPage;
		this.totalCount = totalCount;
		this.myCommentDtos = myCommentDtos;
	}

	public static MyCommentResponseDto of(Page<Comment> comments) {
		return new MyCommentResponseDto(
				comments.getTotalPages(),
				comments.getTotalElements(),
				comments.getContent().stream()
						.map(
								comment -> MyCommentDto.of(comment)
						).collect(Collectors.toList())
		);
	}
}
