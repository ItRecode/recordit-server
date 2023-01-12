package com.recordit.server.dto.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleAccessTokenResponseDto {
	private String accessToken;
	private String expiresIn;
	private String scope;
	private String tokenType;
	private String idToken;
}
