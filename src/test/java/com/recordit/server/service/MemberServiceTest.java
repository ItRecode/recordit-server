package com.recordit.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
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
import com.recordit.server.domain.Comment;
import com.recordit.server.domain.Member;
import com.recordit.server.domain.MemberDeleteHistory;
import com.recordit.server.domain.Record;
import com.recordit.server.dto.member.LoginRequestDto;
import com.recordit.server.dto.member.ModifyMemberRequestDto;
import com.recordit.server.dto.member.RegisterRequestDto;
import com.recordit.server.dto.member.RegisterSessionResponseDto;
import com.recordit.server.exception.member.DuplicateNicknameException;
import com.recordit.server.exception.member.MemberNotFoundException;
import com.recordit.server.exception.member.NotFoundRegisterSessionException;
import com.recordit.server.exception.member.NotFoundUserInfoInSessionException;
import com.recordit.server.repository.CommentRepository;
import com.recordit.server.repository.MemberDeleteHistoryRepository;
import com.recordit.server.repository.MemberRepository;
import com.recordit.server.repository.RecordRepository;
import com.recordit.server.service.oauth.OauthService;
import com.recordit.server.service.oauth.OauthServiceLocator;
import com.recordit.server.util.RedisManager;
import com.recordit.server.util.SessionUtil;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberDeleteHistoryRepository memberDeleteHistoryRepository;

	@Mock
	private RecordRepository recordRepository;

	@Mock
	private CommentRepository commentRepository;

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

	@Mock
	private ImageFileService imageFileService;

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

	@Nested
	@DisplayName("로그인 된 사용자의 닉네임을 응답하는 기능에서")
	class 로그인_된_사용자의_닉네임을_응답하는_기능에서 {

		@Test
		@DisplayName("로그인이 되어있지 않다면 예외를 던진다")
		void 로그인이_되어있지_않으면_예외를_던진다() {
			// given
			given(sessionUtil.findUserIdBySession())
					.willThrow(new NotFoundUserInfoInSessionException("세션에 사용자 정보가 저장되어 있지 않습니다"));

			// when, then
			assertThatThrownBy(() -> memberService.findNicknameIfPresent())
					.isInstanceOf(NotFoundUserInfoInSessionException.class);
		}

		@Test
		@DisplayName("세션에 회원의 아이디는 존재하지만 테이블에 없다면 예외를 던진다")
		void 세션에_회원의_아이디는_존재하지만_테이블에_없다면_예외를_던진다() {
			// given
			given(sessionUtil.findUserIdBySession())
					.willReturn(1L);
			given(memberRepository.findById(any()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> memberService.findNicknameIfPresent())
					.isInstanceOf(MemberNotFoundException.class);
		}

		@Test
		@DisplayName("정상적으로 로그인되어있고 테이블에도 정보가 있는 경우엔 예외를 던지지 않는다")
		void 정상적으로_로그인되어있고_테이블에도_정보가_있는_경우엔_예외를_던지지_않는다() {
			//given
			given(sessionUtil.findUserIdBySession())
					.willReturn(1L);
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(mockMember));
			given(mockMember.getNickname())
					.willReturn(nickname);

			// when, then
			assertThatCode(() -> memberService.findNicknameIfPresent())
					.doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("회원정보_변경_에서")
	class 회원정보_변경_에서 {
		private Long memberId = 1L;

		private ModifyMemberRequestDto modifyMemberRequestDto = ModifyMemberRequestDto.builder()
				.nickName("변경된 닉네임")
				.build();

		@Test
		@DisplayName("회원_정보를_찾을 수 없다면 예외를 던진다")
		void 회원_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			given(sessionUtil.findUserIdBySession())
					.willReturn(memberId);

			given(memberRepository.findById(memberId))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> memberService.modifyMember(modifyMemberRequestDto))
					.isInstanceOf(MemberNotFoundException.class)
					.hasMessage("회원 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("변경할_닉네임이_이미_사용중이라면_예외를_던진다")
		void 변경할_닉네임이_이미_사용중이라면_예외를_던진다() {
			// given
			given(sessionUtil.findUserIdBySession())
					.willReturn(memberId);

			given(memberRepository.findById(memberId))
					.willReturn(Optional.of(mockMember));

			given(memberRepository.existsByNickname(anyString()))
					.willReturn(true);

			// when, then
			assertThatThrownBy(() -> memberService.modifyMember(modifyMemberRequestDto))
					.isInstanceOf(DuplicateNicknameException.class)
					.hasMessage("중복된 닉네임이 존재합니다.");
		}

		@Test
		@DisplayName("정상적이라면 예외를 던지지 않는다")
		void 정상적이라면_예외를_던지지_않는다() {
			//given
			given(sessionUtil.findUserIdBySession())
					.willReturn(memberId);

			given(memberRepository.findById(memberId))
					.willReturn(Optional.of(mockMember));

			//when, then
			assertThatCode(() -> memberService.modifyMember(modifyMemberRequestDto))
					.doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("로그아웃_에서")
	class 로그아웃_에서 {
		Long memberId = 1L;

		@Test
		@DisplayName("정상적이라면 예외를 던지지 않는다")
		void 정상적이라면_예외를_던지지_않는다() {
			//when, then
			assertThatCode(() -> memberService.logout())
					.doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("회원탈퇴_에서")
	class 회원탈퇴_에서 {
		private Long memberId = 1L;

		@Test
		@DisplayName("회원_정보를_찾을 수 없다면 예외를 던진다")
		void 회원_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			given(sessionUtil.findUserIdBySession())
					.willReturn(memberId);

			given(memberRepository.findById(memberId))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> memberService.deleteMember())
					.isInstanceOf(MemberNotFoundException.class)
					.hasMessage("회원 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("정상적이라면 예외를 던지지 않는다")
		void 정상적이라면_예외를_던지지_않는다() {
			// given
			Record mockRecord = mock(Record.class);
			Comment mockComment = mock(Comment.class);

			given(sessionUtil.findUserIdBySession()).willReturn(memberId);
			given(memberRepository.findById(memberId)).willReturn(Optional.of(mockMember));
			given(recordRepository.findAllByWriter(mockMember)).willReturn(List.of(mockRecord));
			given(commentRepository.findAllByWriter(mockMember)).willReturn(List.of(mockComment));

			// when
			Long deletedUserId = memberService.deleteMember();

			// then
			assertThat(deletedUserId).isEqualTo(memberId);

			verify(memberRepository, times(1)).delete(mockMember);
			verify(memberDeleteHistoryRepository, times(1)).save(any(MemberDeleteHistory.class));
			verify(recordRepository, times(1)).deleteByWriter(mockMember);
			verify(commentRepository, times(1)).deleteByWriter(mockMember);
			verify(sessionUtil, times(1)).invalidateSession();
		}
	}
}
