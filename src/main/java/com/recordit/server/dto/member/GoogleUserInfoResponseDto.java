package com.recordit.server.dto.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleUserInfoResponseDto {
	private String iss;
	private String azp;
	private String aud;
	private String sub;
	private String atHash;
	private String name;
	private String picture;
	private String givenName;
	private String familyName;
	private String locale;
	private String iat;
	private String exp;
	private String alg;
	private String kid;
	private String typ;
}
