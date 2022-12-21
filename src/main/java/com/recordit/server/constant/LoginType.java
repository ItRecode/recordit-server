package com.recordit.server.constant;

import java.util.Arrays;

public enum LoginType {
	LOCAL,
	KAKAO,
	GOOGLE;

	public static LoginType findByString(String str) {
		return Arrays.stream(LoginType.values())
				.filter(loginType -> loginType.name().equals(str))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("일치하는 로그인 타입이 없습니다."));
	}
}
