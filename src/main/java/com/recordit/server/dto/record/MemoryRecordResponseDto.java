package com.recordit.server.dto.record;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Slice;

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
	@ApiModelProperty(notes = "다음 페이지가 있는지 여부")
	private Boolean hasNextPage;

	@ApiModelProperty(notes = "처음 페이지 인지 여부")
	private Boolean isFirstPage;

	@ApiModelProperty(notes = "마지막 페이지 인지 여부")
	private Boolean isLastPage;

	@ApiModelProperty(notes = "추억 레코드 리스트")
	private List<MemoryRecordDto> memoryRecordList;

	@Builder
	public MemoryRecordResponseDto(
			Slice<Record> memoryRecordSlice,
			List<List<Comment>> commentList
	) {
		this.hasNextPage = memoryRecordSlice.hasNext();
		this.isFirstPage = memoryRecordSlice.isFirst();
		this.isLastPage = memoryRecordSlice.isLast();
		this.memoryRecordList = new ArrayList<>();

		for (int i = 0; i < memoryRecordSlice.getContent().size(); i++) {
			memoryRecordList.add(
					MemoryRecordDto.builder()
							.recordId(memoryRecordSlice.getContent().get(i).getId())
							.title(memoryRecordSlice.getContent().get(i).getTitle())
							.iconColor(memoryRecordSlice.getContent().get(i).getRecordColor().getName())
							.iconName(memoryRecordSlice.getContent().get(i).getRecordIcon().getName())
							.commentList(MemoryRecordCommentDto.asMemoryRecordCommentDtoList(commentList.get(i)))
							.build()
			);
		}
	}
}
