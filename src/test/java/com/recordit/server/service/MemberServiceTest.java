package com.recordit.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.recordit.server.exception.member.DuplicateNicknameException;
import com.recordit.server.repository.MemberRepository;
import com.recordit.server.service.oauth.OauthServiceLocator;
import com.recordit.server.util.RedisManager;
import com.recordit.server.util.SessionUtil;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private OauthServiceLocator oauthServiceLocator;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private SessionUtil sessionUtil;

	@Mock
	RedisManager redisManager;

	@Nested
	@DisplayName("닉네임 중복 확인 기능에서 닉네임이")
	class 닉네임_중복_확인_기능에서_닉네임이 {

		@Test
		@DisplayName("중복되면 예외를 던진다")
		void 중복되면_예외를_던진다() {
			// given
			String nickname = "test";

			given(memberRepository.existsByNickname(anyString()))
					.willReturn(true);

			// when, then
			assertThatThrownBy(() -> memberService.isDuplicateNickname(nickname))
					.isInstanceOf(DuplicateNicknameException.class);
		}

		@Test
		@DisplayName("중복되지 않으면 예외를 던지지 않는다")
		void 중복되지_않으면_예외를_던지지_않는다() {
			// given
			String nickname = "test";

			given(memberRepository.existsByNickname(anyString()))
					.willReturn(false);

			// when, then
			assertThatCode(() -> memberService.isDuplicateNickname(nickname))
					.doesNotThrowAnyException();
		}
	}
}
