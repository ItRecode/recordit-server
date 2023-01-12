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
public class LoginRequestDto {

	@ApiModelProperty(notes = "Oauth API에서 응답 받은 토큰", required = true)
	private String oauthToken;

	@Builder
	public LoginRequestDto(String oauthToken) {
		this.oauthToken = oauthToken;
	}
}
