package com.recordit.server.dto.record;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import com.recordit.server.domain.Record;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecentOneRecordResponseDto {
	@ApiParam(value = "레코드 ID", required = true)
	private Long recordId;

	@ApiParam(value = "레코드 제목", required = true)
	private String title;

	@ApiParam(value = "레코드 컬러명", required = true)
	private String colorName;

	@ApiParam(value = "레코드 아이콘명", required = true)
	private String iconName;

	@ApiParam(value = "댓글 개수", required = true)
	private Long commentCount;

	@ApiParam(value = "작성 날짜", required = true)
	private LocalDateTime createdAt;

	private RecentOneRecordResponseDto(Long recordId, String title, String colorName, String iconName,
			Long commentCount,
			LocalDateTime createdAt) {
		this.recordId = recordId;
		this.title = title;
		this.colorName = colorName;
		this.iconName = iconName;
		this.commentCount = commentCount;
		this.createdAt = createdAt;
	}

	public static RecentOneRecordResponseDto of(
			Record record
	) {
		return new RecentOneRecordResponseDto(
				record.getId(),
				record.getTitle(),
				record.getRecordColor().getName(),
				record.getRecordIcon().getName(),
				Long.valueOf(
						record.getComments()
								.stream()
								.filter(comment -> comment.getParentComment() == null)
								.collect(Collectors.toList())
								.size()
				),
				record.getCreatedAt()
		);
	}
}
