package com.recordit.server.dto.record.memory;

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
public class MemoryRecordDto {
	@ApiModelProperty(notes = "추억 레코드 아이디")
	private Long recordId;

	@ApiModelProperty(notes = "추억 레코드 제목")
	private String title;

	@ApiModelProperty(notes = "추억 레코드 아이콘 이름")
	private String iconName;

	@ApiModelProperty(notes = "추억 레코드 컬러 이름")
	private String colorName;

	@ApiModelProperty(notes = "추억 레코드 댓글 리스트")
	private List<MemoryRecordCommentDto> memoryRecordComments;

	private MemoryRecordDto(
			Long recordId,
			String title,
			String iconName,
			String colorName,
			List<MemoryRecordCommentDto> memoryRecordComments
	) {
		this.recordId = recordId;
		this.title = title;
		this.iconName = iconName;
		this.colorName = colorName;
		this.memoryRecordComments = memoryRecordComments;
	}

	public static MemoryRecordDto of(
			Record record,
			List<Comment> memoryRecordComments
	) {
		return new MemoryRecordDto(
				record.getId(),
				record.getTitle(),
				record.getRecordIcon().getName(),
				record.getRecordColor().getName(),
				memoryRecordComments.stream()
						.map(comment -> MemoryRecordCommentDto.of(comment))
						.collect(Collectors.toList())
		);
	}
}
