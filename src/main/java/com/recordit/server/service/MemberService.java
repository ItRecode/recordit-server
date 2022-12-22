package com.recordit.server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recordit.server.dto.member.LoginRequestDto;
import com.recordit.server.dto.member.RegisterRequestDto;
import com.recordit.server.dto.member.RegisterSessionResponseDto;
import com.recordit.server.repository.MemberRepository;
import com.recordit.server.service.oauth.OauthService;
import com.recordit.server.service.oauth.OauthServiceLocator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final OauthServiceLocator oauthServiceLocator;
	private final MemberRepository memberRepository;

	@Transactional
	public RegisterSessionResponseDto oauthLogin(String loginType, LoginRequestDto loginRequestDto) {
		OauthService oauthService = oauthServiceLocator.getOauthServiceByLoginType(loginType);
		return null;
	}

	@Transactional
	public void oauthRegister(String loginType, RegisterRequestDto registerRequestDto) {
		OauthService oauthService = oauthServiceLocator.getOauthServiceByLoginType(loginType);
	}

	@Transactional(readOnly = true)
	public void isDuplicateNickname(String nickname) {
	}
}
