package com.recodeit.server.service.oauth;

import com.recodeit.server.constant.LoginType;

public interface OauthService {

    LoginType getLoginType();

    void request();
}
