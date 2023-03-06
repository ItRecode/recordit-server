package com.recordit.server.dto.comment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.recordit.server.domain.Comment;
import com.recordit.server.domain.Record;

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
	@ApiModelProperty(notes = "내가 작성한 댓글이 속한 레코드의 전체 페이지 개수", required = true)
	private Integer totalPage;

	@ApiModelProperty(notes = "내가 작성한 댓글이 속한 레코드의 전체 개수", required = true)
	private Long totalCount;

	@ApiModelProperty(notes = "내가 작성한 댓글이 속한 레코드정보와 내가 작성한 댓글 리스트", required = true)
	private List<MyCommentsDto> myCommentsDtos;

	private MyCommentResponseDto(
			Integer totalPage,
			Long totalCount,
			List<MyCommentsDto> myCommentsDtos
	) {
		this.totalPage = totalPage;
		this.totalCount = totalCount;
		this.myCommentsDtos = myCommentsDtos;
	}

	public static MyCommentResponseDto of(
			Page<Record> recordPage,
			LinkedHashMap<Record, List<Comment>> recordListLinkedHashMap
	) {
		return new MyCommentResponseDto(
				recordPage.getTotalPages(),
				recordPage.getTotalElements(),
				recordListLinkedHashMap.keySet().stream().map(
						record -> MyCommentsDto.of(record, recordListLinkedHashMap.get(record))
				).collect(Collectors.toList())
		);
	}
}
