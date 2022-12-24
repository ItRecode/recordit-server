package com.recordit.server.dto.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoAccessTokenResponseDto {
	private String accessToken;
	private String tokenType;
	private String refreshToken;
	private String refreshTokenExpiresIn;
	private String expiresIn;
	private String scope;
}

