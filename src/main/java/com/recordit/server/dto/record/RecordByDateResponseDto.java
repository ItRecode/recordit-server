package com.recordit.server.dto.record;

import java.util.LinkedHashMap;
import java.util.List;
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
public class RecordByDateResponseDto {

	@ApiModelProperty(notes = "요청 레코드의 전체 페이지 개수", required = true)
	private Integer totalPage;

	@ApiModelProperty(notes = "요청 레코드의 전체 개수", required = true)
	private Long totalCount;

	@ApiModelProperty(notes = "레코드 리스트", required = true)
	private List<RecordByDateDto> recordByDateDtos;

	@Builder
	public RecordByDateResponseDto(
			Page<Record> records,
			LinkedHashMap<Record, Long> recordToNumOfComments
	) {
		this.totalPage = records.getTotalPages();
		this.totalCount = records.getTotalElements();
		this.recordByDateDtos = records.stream()
				.map(
						record -> RecordByDateDto.builder()
								.record(record)
								.commentCount(recordToNumOfComments.get(record))
								.build()
				).collect(Collectors.toList());
	}
}
