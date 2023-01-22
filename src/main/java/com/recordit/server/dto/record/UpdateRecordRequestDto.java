package com.recordit.server.dto.record;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class UpdateRecordRequestDto {
	@Size(max = 12, message = "레코드 제목은 최대 12자 입니다.")
	@NotBlank(message = "레코드 제목은 빈 값일 수 없습니다.")
	private String title;

	@Size(max = 200, message = "레코드 내용은 최대 200자 입니다.")
	@NotBlank(message = "레코드 내용은 빈 값일 수 없습니다.")
	private String content;

	@NotBlank(message = "컬러 이름은 빈 값일 수 없습니다.")
	private String colorName;

	@NotBlank(message = "아이콘 이름은 빈 값일 수 없습니다.")
	private String iconName;

	private List<String> deleteImages;

	@Builder

	public UpdateRecordRequestDto(String title, String content, String colorName, String iconName,
			List<String> deleteImages) {
		this.title = title;
		this.content = content;
		this.colorName = colorName;
		this.iconName = iconName;
		this.deleteImages = deleteImages;
	}
}
