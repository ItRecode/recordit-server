package com.recordit.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recordit.server.service.MemberService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;

	@ApiOperation(value = "로그인",
			notes = "로그인 타입과 세션을 받아 가입된 회원이라면 로그인을 진행시키고 Header에 Set-cookie: SESSION=;응답합니다")
	@ApiResponses({
			@ApiResponse(code = 200, message = "API 정상 작동 / Header에 Set-cookie: SESSION=;응답"),
			@ApiResponse(code = 401, message = "회원정보가 없어 회원가입이 필요한 경우입니다."
					+ "Body로 응답된 register_session과 사용자에게 닉네임을 받아 '/member/oauth/register/{loginType}'으로 요청하세요")
	})
	@PostMapping("/oauth/login/{loginType}")
	public void oauthLogin(
			@PathVariable("loginType") String loginType, @RequestBody String token) {
		memberService.oauthLogin(loginType);
	}

	@ApiOperation(value = "회원가입",
			notes = "로그인 타입, TEMPSESSIONID, 닉네임을 받아 회원가입을 진행합니다")
	@ApiResponses({
			@ApiResponse(code = 200, message = "API 정상 작동 / 세션 로그인 처리하고 Header에 Set-cookie: SESSION=; 헤더로 응답"),
			@ApiResponse(code = 428, message = "register_session 정보가 Redis에 없거나 비정상적일 경우"),
			@ApiResponse(code = 409, message = "닉네임이 중복 된 경우")
	})
	@PostMapping("/oauth/register/{loginType}")
	public void oauthRegister(@PathVariable("loginType") String loginType, @RequestBody String nickname) {
		memberService.oauthRegister(loginType);
	}

	@ApiOperation(value = "닉네임 중복확인",
			notes = "닉네임을 받아 해당 닉네임이 중복되었는지 판별")
	@ApiResponses({
			@ApiResponse(code = 200, message = "닉네임이 사용 가능한 경우"),
			@ApiResponse(code = 409, message = "닉네임이 중복 된 경우")
	})
	@GetMapping
	public void duplicateNicknameCheck(@RequestBody String nickname) {

	}
}
