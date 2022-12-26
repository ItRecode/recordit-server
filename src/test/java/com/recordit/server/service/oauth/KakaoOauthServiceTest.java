package com.recordit.server.service.oauth;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.recordit.server.constant.LoginType;
import com.recordit.server.environment.KakaoOauthProperties;

@ExtendWith(MockitoExtension.class)
class KakaoOauthServiceTest {

	@InjectMocks
	private KakaoOauthService kakaoOauthService;

	@Mock
	private KakaoOauthProperties kakaoOauthProperties;

	@Test
	@DisplayName("LoginType이 정상적으로 응답되는지 테스트한다")
	void LoginType이_정상적으로_응답되는지_테스트한다() {
		// given

		// when
		LoginType loginType = kakaoOauthService.getLoginType();

		// then
		assertThat(loginType).isEqualTo(LoginType.KAKAO);
	}
}