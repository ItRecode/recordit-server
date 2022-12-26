package com.recordit.server.dto.record;

import java.time.LocalDateTime;

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
	private String title;
	private String content;
	private String hex;
	private String iconName;
	private LocalDateTime createdAt;
	private String imageUrl;

	@Builder
	public RecordDetailResponseDto(String title, String content, String hex, String iconName, LocalDateTime createdAt,
			String imageUrl) {
		this.title = title;
		this.content = content;
		this.hex = hex;
		this.iconName = iconName;
		this.createdAt = createdAt;
		this.imageUrl = imageUrl;
	}
}
