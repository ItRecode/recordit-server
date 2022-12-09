package com.recodeit.server.service;

import com.recodeit.server.service.oauth.OauthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final List<OauthService> oauthServices;

    public void oauthLogin(String loginType) {

    }

    public void oauthRegister(String loginType) {

    }
}
