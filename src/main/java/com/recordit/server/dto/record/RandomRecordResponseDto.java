package com.recordit.server.dto.record;

import com.recordit.server.domain.Record;

import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RandomRecordResponseDto {
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

	public static RandomRecordResponseDto of(
			Record record,
			Long commentCount
	) {
		return RandomRecordResponseDto.builder()
				.recordId(record.getId())
				.title(record.getTitle())
				.colorName(record.getRecordColor().getName())
				.iconName(record.getRecordIcon().getName())
				.commentCount(commentCount)
				.build();
	}
}
