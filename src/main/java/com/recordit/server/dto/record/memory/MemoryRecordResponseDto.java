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

	private MemoryRecordResponseDto(
			Integer totalPage,
			Long totalCount,
			List<MemoryRecordDto> memoryRecordList
	) {
		this.totalPage = totalPage;
		this.totalCount = totalCount;
		this.memoryRecordList = memoryRecordList;
	}

	public static MemoryRecordResponseDto of(
			Page<Record> memoryRecords,
			Map<Record, List<Comment>> recordToComments
	) {
		return new MemoryRecordResponseDto(
				memoryRecords.getTotalPages(),
				memoryRecords.getTotalElements(),
				memoryRecords.stream()
						.map(record -> MemoryRecordDto.of(record, recordToComments.get(record)))
						.collect(Collectors.toList())
		);
	}
}
