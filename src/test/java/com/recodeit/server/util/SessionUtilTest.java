package com.recodeit.server.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import com.recodeit.server.exception.NotFoundUserInfoInSessionException;

@ExtendWith(MockitoExtension.class)
public class SessionUtilTest {

	@InjectMocks
	private SessionUtil sessionUtil;

	@Spy
	MockHttpSession mockHttpSession;

	@Test
	@DisplayName("세션에 userId를 저장하는 기능을 테스트한다")
	void 세션에_UserId를_저장하는_기능을_테스트한다() {
		// given

		// when
		sessionUtil.saveUserIdInSession(1L);

		// then
		assertThat(mockHttpSession.getAttribute("LOGIN_USER_ID")).isEqualTo((Long)1L);
	}

	@Nested
	@DisplayName("세션에서 userId를 찾을 때")
	class 세션에서_userId를_찾을_때 {

		@Test
		@DisplayName("세션에 userId가 있으면 값이 찾아와진다")
		void 세션에_정상적으로_userId를_저장하고_있으면_정상적으로_값이_찾아와진다() {
			// given
			long userId = 1L;
			mockHttpSession.setAttribute("LOGIN_USER_ID", userId);

			// when
			Long userIdBySession = sessionUtil.findUserIdBySession();

			// then
			assertThat(userIdBySession.longValue()).isEqualTo(userId);
			assertThatCode(() -> sessionUtil.findUserIdBySession()).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("세션에 userId가 없으면 예외를 던진다")
		void 세션에_userId가_없으면_예외를_던진다() {
			// given

			// when, then
			assertThatThrownBy(() -> sessionUtil.findUserIdBySession())
					.isInstanceOf(NotFoundUserInfoInSessionException.class);

		}
	}

	@Test
	@DisplayName("세션을 삭제하는 기능을 테스트한다")
	void 세션을_삭제하는_기능을_테스트한다() {
		// given

		// when
		sessionUtil.removeSession();

		// then
		assertThat(mockHttpSession.isInvalid()).isTrue();
	}

}