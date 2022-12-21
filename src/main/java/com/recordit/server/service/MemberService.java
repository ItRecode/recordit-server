package com.recordit.server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recordit.server.service.oauth.OauthService;
import com.recordit.server.service.oauth.OauthServiceLocator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final OauthServiceLocator oauthServiceLocator;

	@Transactional
	public void oauthLogin(String loginType) {
		OauthService oauthService = oauthServiceLocator.getOauthServiceByLoginType(loginType);
	}

	@Transactional
	public void oauthRegister(String loginType) {
		OauthService oauthService = oauthServiceLocator.getOauthServiceByLoginType(loginType);
	}
}
