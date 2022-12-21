package com.recordit.server.service.oauth;

import com.recordit.server.constant.LoginType;

public class GoogleOauthService implements OauthService {
	@Override
	public LoginType getLoginType() {
		return LoginType.GOOGLE;
	}

	@Override
	public void request() {

	}
}
