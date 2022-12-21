package com.recordit.server.service.oauth;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OauthServiceLocator {
	private final List<OauthService> oauthServices;

	public OauthService getOauthServiceByLoginType(String loginType) {
		if (!StringUtils.hasText(loginType)) {
			throw new NullPointerException("로그인 타입이 없습니다.");
		}
		for (OauthService service : oauthServices) {
			if (loginType.equals(service.getLoginType().name())) {
				return service;
			}
		}
		throw new NullPointerException("일치하는 로그인 타입이 없습니다.");
	}
}
