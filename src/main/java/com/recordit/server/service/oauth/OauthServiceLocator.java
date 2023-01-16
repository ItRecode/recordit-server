package com.recordit.server.service.oauth;

import java.util.List;

import org.springframework.stereotype.Component;

import com.recordit.server.constant.LoginType;
import com.recordit.server.exception.member.NotMatchLoginTypeException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OauthServiceLocator {
	private final List<OauthService> oauthServices;

	public OauthService getOauthServiceByLoginType(LoginType loginType) {
		return oauthServices.stream()
				.filter(oauthService -> oauthService.getLoginType() == loginType)
				.findFirst()
				.orElseThrow(() -> new NotMatchLoginTypeException("일치하는 로그인 타입이 없습니다."));
	}
}
