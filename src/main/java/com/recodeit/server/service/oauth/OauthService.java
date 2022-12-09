package com.recodeit.server.service.oauth;

import com.recodeit.server.constant.LoginType;

public interface OauthService {

    LoginType getLoginType(String loginType);

    void request();
}
