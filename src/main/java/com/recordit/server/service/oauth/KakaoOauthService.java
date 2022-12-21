package com.recordit.server.service.oauth;

import com.recordit.server.constant.LoginType;

public class KakaoOauthService implements OauthService {
	@Override
	public LoginType getLoginType() {
		return LoginType.KAKAO;
	}

	@Override
	public void request() {

	}
}
