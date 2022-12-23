package com.recordit.server.dto.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequestDto {

	@ApiModelProperty(notes = "회원가입시 필요한 임시 Session", required = true)
	private String registerSession;

	@ApiModelProperty(notes = "사용자 닉네임", required = true)
	private String nickname;

	@Builder
	public RegisterRequestDto(String registerSession, String nickname) {
		this.registerSession = registerSession;
		this.nickname = nickname;
	}
}
