package com.recodeit.server.service.oauth;

import com.recodeit.server.constant.LoginType;

public class GoogleOauthService implements OauthService {
    @Override
    public LoginType getLoginType(String loginType) {
        return LoginType.GOOGLE;
    }

    @Override
    public void request() {

    }
}
