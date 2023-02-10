package com.recordit.server.dto.record.memory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.recordit.server.domain.Comment;
import com.recordit.server.domain.Record;

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
public class MemoryRecordResponseDto {

	@ApiModelProperty(notes = "요청 추억 레코드의 전체 페이지 개수", required = true)
	private Integer totalPage;

	@ApiModelProperty(notes = "요청 추억 레코드의 전체 개수", required = true)
	private Long totalCount;

	@ApiModelProperty(notes = "추억 레코드 리스트")
	private List<MemoryRecordDto> memoryRecordList;

	@Builder
	public MemoryRecordResponseDto(
			Page<Record> memoryRecords,
			Map<Record, List<Comment>> recordToComments
	) {
		this.totalPage = memoryRecords.getTotalPages();
		this.totalCount = memoryRecords.getTotalElements();
		this.memoryRecordList = memoryRecords.stream()
				.map(
						record -> MemoryRecordDto.builder()
								.recordId(record.getId())
								.title(record.getTitle())
								.colorName(record.getRecordColor().getName())
								.iconName(record.getRecordIcon().getName())
								.memoryRecordComments(recordToComments.get(record))
								.build()
				).collect(Collectors.toList());
	}
}
