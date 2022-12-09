package com.recodeit.server.service.oauth;

import com.recodeit.server.constant.LoginType;

public class KakaoOauthService implements OauthService {
    @Override
    public LoginType getLoginType() {
        return LoginType.KAKAO;
    }

    @Override
    public void request() {

    }
}
