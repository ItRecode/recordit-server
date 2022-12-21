package com.recordit.server.service.oauth;

import org.springframework.stereotype.Service;

import com.recordit.server.constant.LoginType;

@Service
public class KakaoOauthService implements OauthService {
	@Override
	public LoginType getLoginType() {
		return LoginType.KAKAO;
	}

	@Override
	public void request() {

	}
}
