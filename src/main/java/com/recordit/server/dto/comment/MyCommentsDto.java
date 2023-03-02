package com.recordit.server.dto.comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
public class MyCommentsDto {
	@ApiModelProperty(notes = "레코드의 카테고리 이름")
	private String categoryName;

	@ApiModelProperty(notes = "레코드 아이디")
	private Long recordId;

	@ApiModelProperty(notes = "레코드의 제목")
	private String title;

	@ApiModelProperty(notes = "레코드의 아이콘 이름")
	private String iconName;

	@ApiModelProperty(notes = "레코드의 색상 이름")
	private String colorName;

	@ApiModelProperty(notes = "레코드 작성 시각")
	private LocalDateTime recordCreatedAt;

	@ApiModelProperty(notes = "레코드에 달린 모든 댓글 갯수")
	private Integer commentsCount;

	@ApiModelProperty(notes = "내가 작성한 댓글 리스트")
	private List<MyCommentDto> myCommentDtos;

	private MyCommentsDto(
			String categoryName,
			Long recordId,
			String title,
			String iconName,
			String colorName,
			Integer commentsCount,
			LocalDateTime recordCreatedAt,
			List<MyCommentDto> myCommentDtos
	) {
		this.categoryName = categoryName;
		this.recordId = recordId;
		this.title = title;
		this.iconName = iconName;
		this.colorName = colorName;
		this.commentsCount = commentsCount;
		this.recordCreatedAt = recordCreatedAt;
		this.myCommentDtos = myCommentDtos;
	}

	public static MyCommentsDto of(
			Record record,
			List<Comment> myComments
	) {
		return new MyCommentsDto(
				record.getRecordCategory().getName(),
				record.getId(),
				record.getTitle(),
				record.getRecordIcon().getName(),
				record.getRecordColor().getName(),
				record.getComments().size(),
				record.getCreatedAt(),
				myComments.stream().map(
						comment -> MyCommentDto.of(comment)
				).collect(Collectors.toList())
		);
	}
}
