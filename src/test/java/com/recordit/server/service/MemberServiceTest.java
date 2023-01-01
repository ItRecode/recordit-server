package com.recordit.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

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
	private RedisManager redisManager;

	@Mock
	private Member mockMember;

	private MockedStatic<UUID> mockUUID;

	private final LoginType loginType = Arrays.stream(LoginType.values())
			.findFirst()
			.get();

	private final UUID testUUID = UUID.randomUUID();

	private final String mockOauthId = "testOauthId";
	private final String nickname = "testNickname";

	@BeforeEach
	void init() {
		mockUUID = mockStatic(UUID.class);
	}

	@AfterEach
	void afterEach() {
		mockUUID.close();
	}

	@Nested
	@DisplayName("oauth 로그인을 할 때")
	class oauth_로그인을_할_때_oauthToken을_통해_찾은_사용자가 {

		@Nested
		@DisplayName("oauthToken을 통해 찾은 사용자가")
		class oauthToken을_통해_찾은_사용자가 {

			@Mock
			private OauthService oauthService;

			private final String mockOauthToken = "testOauthToken";

			private final LoginRequestDto loginRequestDto = LoginRequestDto.builder()
					.oauthToken(mockOauthToken)
					.build();

			@Test
			@DisplayName("있으면 null을 반환한다")
			void 있으면_null을_반환한다() {
				// given
				Optional<Member> mockMember = Optional.of(MemberServiceTest.this.mockMember);

				given(oauthServiceLocator.getOauthServiceByLoginType(any()))
						.willReturn(oauthService);
				given(oauthService.getUserInfoByOauthToken(anyString()))
						.willReturn(mockOauthId);
				given(memberRepository.findByOauthId(mockOauthId))
						.willReturn(mockMember);

				// when
				Optional<RegisterSessionResponseDto> result = memberService.oauthLogin(
						loginType,
						loginRequestDto
				);

				// then
				assertThat(result.isEmpty()).isTrue();
			}

			@Test
			@DisplayName("없으면 registerSessionUUID를 반환한다")
			void 없으면_registerSessionUUID를_반환한다() {
				// given
				given(oauthServiceLocator.getOauthServiceByLoginType(any()))
						.willReturn(oauthService);
				given(oauthService.getUserInfoByOauthToken(anyString()))
						.willReturn(mockOauthId);
				given(memberRepository.findByOauthId(mockOauthId))
						.willReturn(Optional.empty());
				given(UUID.randomUUID())
						.willReturn(testUUID);

				// when
				Optional<RegisterSessionResponseDto> result = memberService.oauthLogin(
						loginType,
						loginRequestDto
				);

				// then
				assertThat(result.isPresent()).isTrue();
				assertThat(result.get().getRegisterSession()).isEqualTo(testUUID.toString());
			}
		}
	}

	@Nested
	@DisplayName("oauth 회원가입을 할 때")
	class oauth_회원가입을_할_때 {

		@Nested
		@DisplayName("Redis에 RegisterSession이")
		class Redis에_RegisterSession이 {

			private RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
					.registerSession(testUUID.toString())
					.nickname(nickname)
					.build();

			@Test
			@DisplayName("없으면 예외를 던진다")
			void 없으면_예외를_던진다() {
				// given
				given(redisManager.get(anyString(), any()))
						.willReturn(Optional.empty());

				// when, then
				assertThatThrownBy(() -> memberService.oauthRegister(
						loginType,
						registerRequestDto
				)).isInstanceOf(NotFoundRegisterSessionException.class);

			}

			@Test
			@DisplayName("있으면 예외를 던지지 않는다")
			void 있으면_예외를_던지지_않는다() {
				// given
				given(redisManager.get(anyString(), any()))
						.willReturn(Optional.of(mockOauthId));
				given(memberRepository.existsByNickname(anyString()))
						.willReturn(false);
				given(memberRepository.save(any())).willReturn(mockMember);
				willDoNothing().given(sessionUtil).saveUserIdInSession(anyLong());

				// when, then
				assertThatCode(() -> memberService.oauthRegister(loginType, registerRequestDto))
						.doesNotThrowAnyException();
			}

		}
	}

	@Nested
	@DisplayName("닉네임 중복 확인 기능에서")
	class 닉네임_중복_확인_기능에서_닉네임이 {

		@Test
		@DisplayName("중복되면 예외를 던진다")
		void 중복되면_예외를_던진다() {
			// given
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
			given(memberRepository.existsByNickname(anyString()))
					.willReturn(false);

			// when, then
			assertThatCode(() -> memberService.isDuplicateNickname(nickname))
					.doesNotThrowAnyException();
		}
	}
}
