package com.recordit.server.constant;

import java.util.Arrays;

import com.recordit.server.exception.member.NotMatchLoginTypeException;

public enum LoginType {
	LOCAL,
	KAKAO,
	GOOGLE;

	public static LoginType findByString(String str) {
		return Arrays.stream(LoginType.values())
				.filter(loginType -> loginType.name().equals(str))
				.findFirst()
				.orElseThrow(() -> new NotMatchLoginTypeException("일치하는 로그인 타입이 없습니다."));
	}
}
