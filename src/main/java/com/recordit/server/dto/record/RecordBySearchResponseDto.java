package com.recordit.server.dto.record;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

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
public class RecordBySearchResponseDto {
	@ApiModelProperty(notes = "요청 레코드의 전체 페이지 개수", required = true)
	private Integer totalPage;

	@ApiModelProperty(notes = "요청 레코드의 전체 개수", required = true)
	private Long totalCount;

	@ApiModelProperty(notes = "레코드 리스트", required = true)
	private List<RecordBySearchDto> recordBySearchDtos;

	@Builder
	public RecordBySearchResponseDto(
			Page<Record> records,
			Map<Record, Long> recordToNumOfComments
	) {
		this.totalPage = records.getTotalPages();
		this.totalCount = records.getTotalElements();
		this.recordBySearchDtos = records.stream()
				.map(
						record -> RecordBySearchDto.builder()
								.record(record)
								.commentCount(recordToNumOfComments.get(record))
								.build()
				).collect(Collectors.toList());
	}
}
