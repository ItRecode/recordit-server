package com.recordit.server.service;

import static com.recordit.server.constant.RegisterSessionConstants.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recordit.server.constant.LoginType;
import com.recordit.server.domain.Member;
import com.recordit.server.dto.member.LoginRequestDto;
import com.recordit.server.dto.member.RegisterRequestDto;
import com.recordit.server.dto.member.RegisterSessionResponseDto;
import com.recordit.server.exception.member.DuplicateNicknameException;
import com.recordit.server.exception.member.NotFoundRegisterSessionException;
import com.recordit.server.repository.MemberRepository;
import com.recordit.server.service.oauth.OauthService;
import com.recordit.server.service.oauth.OauthServiceLocator;
import com.recordit.server.util.RedisManager;
import com.recordit.server.util.SessionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final OauthServiceLocator oauthServiceLocator;
	private final MemberRepository memberRepository;
	private final SessionUtil sessionUtil;
	private final RedisManager redisManager;

	@Transactional
	public Optional<RegisterSessionResponseDto> oauthLogin(String loginType, LoginRequestDto loginRequestDto) {
		OauthService oauthService = oauthServiceLocator.getOauthServiceByLoginType(loginType);

		String oauthId = oauthService.getUserInfoByOauthToken(loginRequestDto.getOauthToken());
		Optional<Member> findMember = memberRepository.findByOauthId(oauthId);

		if (findMember.isPresent()) {
			sessionUtil.saveUserIdInSession(findMember.get().getId());
			return Optional.empty();
		}
		String registerSessionUUID = UUID.randomUUID().toString();
		redisManager.set(PREFIX_REGISTER_SESSION + registerSessionUUID, oauthId, TIMEOUT, TimeUnit.MINUTES);
		return Optional.of(RegisterSessionResponseDto.builder()
				.registerSession(registerSessionUUID)
				.build());
	}

	@Transactional
	public void oauthRegister(String loginType, RegisterRequestDto registerRequestDto) {
		Optional<String> oauthId = redisManager.get(
				PREFIX_REGISTER_SESSION + registerRequestDto.getRegisterSession(),
				String.class);
		if (oauthId.isEmpty()) {
			throw new NotFoundRegisterSessionException("Oauth 회원가입을 위한 register_session이 존재하지 않습니다.");
		}

		isDuplicateNickname(registerRequestDto.getNickname());
		Member saveMember = memberRepository.save(
				Member.of(
						null,
						null,
						registerRequestDto.getNickname(),
						oauthId.get(),
						LoginType.findByString(loginType)
				)
		);
		sessionUtil.saveUserIdInSession(saveMember.getId());
	}

	@Transactional(readOnly = true)
	public void isDuplicateNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new DuplicateNicknameException("중복된 닉네임이 존재합니다.");
		}
	}
}
