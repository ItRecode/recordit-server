package com.recordit.server.dto.record.ranking;

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
public class RecordRankingDto {

	@ApiModelProperty(notes = "레코드 ID")
	private Long recordId;

	@ApiModelProperty(notes = "레코드 제목")
	private String title;

	@ApiModelProperty(notes = "레코드 작성자")
	private String writer;

	@ApiModelProperty(notes = "레코드 컬러 이름")
	private String colorName;

	@ApiModelProperty(notes = "레코드 아이콘 이름")
	private String iconName;

	@ApiModelProperty(notes = "레코드에 달린 댓글의 갯수")
	private Integer numOfComment;

	private RecordRankingDto(Long recordId, String title, String writer, String colorName, String iconName,
			Integer numOfComment) {
		this.recordId = recordId;
		this.title = title;
		this.writer = writer;
		this.colorName = colorName;
		this.iconName = iconName;
		this.numOfComment = numOfComment;
	}

	public static RecordRankingDto of(Record record) {
		return new RecordRankingDto(
				record.getId(),
				record.getTitle(),
				record.getWriter().getNickname(),
				record.getRecordColor().getName(),
				record.getRecordIcon().getName(),
				record.getComments().size()
		);
	}
}
