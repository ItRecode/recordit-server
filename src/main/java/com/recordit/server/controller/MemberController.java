package com.recordit.server.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recordit.server.constant.LoginType;
import com.recordit.server.dto.member.LoginRequestDto;
import com.recordit.server.dto.member.ModifyMemberRequestDto;
import com.recordit.server.dto.member.RegisterRequestDto;
import com.recordit.server.dto.member.RegisterSessionResponseDto;
import com.recordit.server.exception.ErrorMessage;
import com.recordit.server.exception.member.DuplicateNicknameException;
import com.recordit.server.service.MemberService;
import com.recordit.server.util.SessionUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

	private final MemberService memberService;
	private final SessionUtil sessionUtil;

	@ApiOperation(
			value = "Oauth 로그인",
			notes = "로그인 타입과 Oauth 토큰을 통해 Oauth 로그인을 진행합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "API 정상 작동 / Header에 세션 응답",
					responseHeaders = {
							@ResponseHeader(name = "Set-cookie: SESSION=FOO;", description = "FOO = 서버의 세션", response = String.class)
					}
			),
			@ApiResponse(
					code = 400, message = "API에서 지정한 LoginType이 아닐 경우입니다"
			),
			@ApiResponse(
					code = 401, message = "회원정보가 없어 회원가입이 필요한 경우입니다\t\n"
					+ "Body로 응답된 register_session과 사용자에게 닉네임을 받아 '/member/oauth/register/{loginType}'으로 요청하세요",
					response = RegisterSessionResponseDto.class
			)
	})
	@PostMapping("/oauth/login/{loginType}")
	public ResponseEntity oauthLogin(
			@ApiParam(allowableValues = "KAKAO, GOOGLE", required = true) @PathVariable("loginType") LoginType loginType,
			@RequestBody LoginRequestDto loginRequestDto
	) {

		Optional<RegisterSessionResponseDto> registerSessionResponseDto = memberService.oauthLogin(
				loginType,
				loginRequestDto
		);
		if (registerSessionResponseDto.isEmpty()) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(registerSessionResponseDto.get());
	}

	@ApiOperation(
			value = "회원가입",
			notes = "로그인 타입, register_session, 닉네임을 받아 회원가입을 진행합니다"
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "API 정상 작동 / Header에 세션 응답",
					responseHeaders = {
							@ResponseHeader(name = "Set-cookie: SESSION=FOO;", description = "FOO = 서버의 세션", response = String.class)
					}
			),
			@ApiResponse(
					code = 400, message = "API에서 지정한 LoginType이 아닐 경우입니다"
			),
			@ApiResponse(code = 428, message = "register_session 정보가 Redis에 없거나 비정상적일 경우"),
			@ApiResponse(code = 409, message = "닉네임이 중복 된 경우")
	})
	@PostMapping("/oauth/register/{loginType}")
	public ResponseEntity oauthRegister(
			@ApiParam(allowableValues = "KAKAO, GOOGLE", required = true) @PathVariable("loginType") LoginType loginType,
			@RequestBody @Valid RegisterRequestDto registerRequestDto
	) {
		memberService.oauthRegister(loginType, registerRequestDto);
		return new ResponseEntity(HttpStatus.OK);
	}

	@ApiOperation(
			value = "닉네임 중복확인",
			notes = "닉네임을 받아 해당 닉네임이 중복되었는지 판별"
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "닉네임이 사용 가능한 경우 true 반환", response = Boolean.class),
			@ApiResponse(code = 409, message = "닉네임이 중복 된 경우 false 반환")
	})
	@GetMapping("/nickname")
	public ResponseEntity duplicateNicknameCheck(@RequestParam String nickname) {
		try {
			memberService.isDuplicateNickname(nickname);
		} catch (DuplicateNicknameException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(true);
		}
		return ResponseEntity.status(HttpStatus.OK).body(false);
	}

	@ApiOperation(
			value = "세션을 통해 닉네임 조회",
			notes = "로그인 된 회원의 닉네임을 반환합니다"
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "로그인 된 회원의 닉네임 반환", response = String.class),
			@ApiResponse(code = 400, message = "세션에 사용자 정보가 저장되어 있지 않을 때")
	})
	@GetMapping("/auth")
	public ResponseEntity<String> findNicknameIfPresent() {
		return ResponseEntity.status(HttpStatus.OK).body(memberService.findNicknameIfPresent());
	}

	@ApiOperation(
			value = "회원 닉네임 변경",
			notes = "회원 닉네임을 변경합니다."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "변경된 회원의 PK값 반환", response = Long.class),
			@ApiResponse(code = 400, message = "세션에 사용자 정보가 저장되어 있지 않을 때")
	})
	@PutMapping
	public ResponseEntity<Long> modifyMember(@RequestBody @Valid ModifyMemberRequestDto modifyMemberRequestDto) {
		return ResponseEntity.status(HttpStatus.OK).body(memberService.modifyMember(modifyMemberRequestDto));
	}

	@ApiOperation(
			value = "로그아웃",
			notes = "로그아웃"
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "로그아웃 성공", response = Long.class),
	})
	@PostMapping("/logout")
	public void logout() {
		memberService.logout();
	}

	@ApiOperation(
			value = "회원탈퇴",
			notes = "회원탈퇴"
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "회원탈퇴한 회원의 PK값", response = Long.class),
			@ApiResponse(code = 400, message = "잘못된 요청", response = ErrorMessage.class)
	})
	@DeleteMapping("/delete")
	public ResponseEntity<Long> deleteMember() {
		return ResponseEntity.status(HttpStatus.OK).body(memberService.deleteMember());
	}

}
