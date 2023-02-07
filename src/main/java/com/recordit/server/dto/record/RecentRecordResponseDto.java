package com.recordit.server.dto.record;

import com.recordit.server.domain.Record;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecentRecordResponseDto {
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

	private RecentRecordResponseDto(Long recordId, String title, String colorName, String iconName,
			Long commentCount) {
		this.recordId = recordId;
		this.title = title;
		this.colorName = colorName;
		this.iconName = iconName;
		this.commentCount = commentCount;
	}

	public static RecentRecordResponseDto of(
			Record record,
			Long commentCount
	) {
		return new RecentRecordResponseDto(
				record.getId(),
				record.getTitle(),
				record.getRecordColor().getName(),
				record.getRecordIcon().getName(),
				commentCount
		);
	}
}
