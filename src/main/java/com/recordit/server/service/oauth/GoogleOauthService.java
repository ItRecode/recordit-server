package com.recordit.server.service.oauth;

import org.springframework.stereotype.Service;

import com.recordit.server.constant.LoginType;

@Service
public class GoogleOauthService implements OauthService {
	@Override
	public LoginType getLoginType() {
		return LoginType.GOOGLE;
	}

	@Override
	public String request() {
		return null;
	}
}
