package com.recordit.server.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recordit.server.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/member/oauth/{loginType}")
	public void oauthLogin(@PathVariable("loginType") String loginType) {
		memberService.oauthLogin(loginType);
	}

	@PostMapping("/member/oauth/{loginType}")
	public void oauthRegister(@PathVariable("loginType") String loginType) {
		memberService.oauthRegister(loginType);
	}
}
