package com.recordit.server.dto.record;

import java.util.ArrayList;
import java.util.List;

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
public class MemoryRecordCommentDto {
	@ApiModelProperty(notes = "추억레코드 댓글 아이디")
	private Long commentId;

	@ApiModelProperty(notes = "추억레코드 댓글 내용")
	private String content;

	@Builder
	public MemoryRecordCommentDto(Long commentId, String content) {
		this.commentId = commentId;
		this.content = content;
	}

	public static List<MemoryRecordCommentDto> asMemoryRecordCommentDtoList(List<Comment> commentList) {
		List<MemoryRecordCommentDto> memoryRecordCommentDtoList = new ArrayList<>();

		for (Comment comment : commentList) {
			memoryRecordCommentDtoList.add(
					MemoryRecordCommentDto.builder()
							.commentId(comment.getId())
							.content(comment.getContent())
							.build()
			);
		}

		return memoryRecordCommentDtoList;
	}
}
