package com.recordit.server.service.oauth;

import com.recordit.server.constant.LoginType;

public interface OauthService {

	LoginType getLoginType();

	String request();
}
