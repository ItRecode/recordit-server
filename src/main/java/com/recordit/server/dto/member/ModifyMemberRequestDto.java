package com.recordit.server.dto.member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
public class ModifyMemberRequestDto {
	@ApiModelProperty(notes = "변경할 닉네임", required = true)
	@NotBlank(message = "변경할 닉네임은 빈값일 수 없습니다.")
	@Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,8}$", message = "닉네임은 국문,영문,숫자를 포함한 2글자이상 8글자 이하입니다.")
	private String nickName;

	@Builder
	public ModifyMemberRequestDto(String nickName) {
		this.nickName = nickName;
	}
}
