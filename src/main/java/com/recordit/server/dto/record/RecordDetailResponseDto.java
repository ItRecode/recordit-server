package com.recordit.server.dto.record;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@ApiModel
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecordDetailResponseDto {
	private Long recordId;
	private Long categoryId;
	private String categoryName;
	private String title;
	private String content;
	private String writer;
	private String colorName;
	private String iconName;
	private LocalDateTime createdAt;
	private List<String> imageUrls;

	@Builder
	public RecordDetailResponseDto(
			Long recordId,
			Long categoryId,
			String categoryName,
			String title,
			String content,
			String writer,
			String colorName,
			String iconName,
			LocalDateTime createdAt,
			List<String> imageUrls
	) {
		this.recordId = recordId;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.colorName = colorName;
		this.iconName = iconName;
		this.createdAt = createdAt;
		this.imageUrls = imageUrls;
	}
}
