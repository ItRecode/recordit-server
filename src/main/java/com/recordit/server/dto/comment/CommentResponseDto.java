package com.recordit.server.dto.comment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponseDto {

	@ApiModelProperty(notes = "요청 댓글의 전체 페이지 개수", required = true)
	private Integer totalPage;

	@ApiModelProperty(notes = "요청 댓글의 전체 개수", required = true)
	private Long totalCount;

	@ApiModelProperty(notes = "댓글 리스트", required = true)
	private List<CommentDto> commentList;

	@Builder
	public CommentResponseDto(Page<Comment> comments, List<String> imageFileUrls) {
		this.totalPage = comments.getTotalPages();
		this.totalCount = comments.getTotalElements();
		this.commentList = new ArrayList<>();
		for (int i = 0; i < comments.getContent().size(); i++) {
			commentList.add(
					CommentDto.builder()
							.comment(comments.getContent().get(i))
							.imageUrl(imageFileUrls.get(i))
							.build()
			);
		}
	}
}
