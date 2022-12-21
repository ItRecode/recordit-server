package com.recordit.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recordit.server.service.oauth.OauthService;

import lombok.RequiredArgsConstructor;

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
