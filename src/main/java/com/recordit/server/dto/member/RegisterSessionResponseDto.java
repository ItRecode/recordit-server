package com.recordit.server.dto.member;

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
public class RegisterSessionResponseDto {

	@ApiModelProperty(notes = "회원가입시 필요한 임시 Session", required = true)
	private String registerSession;

	@Builder
	public RegisterSessionResponseDto(String registerSession) {
		this.registerSession = registerSession;
	}
}
