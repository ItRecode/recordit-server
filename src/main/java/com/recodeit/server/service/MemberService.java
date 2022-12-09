package com.recodeit.server.service;

import com.recodeit.server.service.oauth.OauthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final List<OauthService> oauthServices;

    @Transactional
    public void oauthLogin(String loginType) {

    }

    @Transactional
    public void oauthRegister(String loginType) {

    }
}
