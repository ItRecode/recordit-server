package com.recordit.server.service.oauth;

import static com.recordit.server.constant.OauthConstants.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.recordit.server.constant.LoginType;
import com.recordit.server.dto.member.KakaoAccessTokenResponseDto;
import com.recordit.server.dto.member.KakaoUserInfoResponseDto;
import com.recordit.server.environment.KakaoOauthProperties;
import com.recordit.server.util.CustomObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOauthService implements OauthService {

	private final KakaoOauthProperties kakaoOauthProperties;

	@Override
	@Transactional(readOnly = true)
	public LoginType getLoginType() {
		return LoginType.KAKAO;
	}

	@Override
	@Transactional(readOnly = true)
	public String getUserInfoByOauthToken(String oauthToken) {
		KakaoAccessTokenResponseDto kakaoAccessTokenResponseDto = requestAccessToken(oauthToken);
		KakaoUserInfoResponseDto kakaoUserInfoResponseDto = requestUserInfo(kakaoAccessTokenResponseDto);
		return String.valueOf(kakaoUserInfoResponseDto.getId());
	}

	@Transactional(readOnly = true)
	protected KakaoAccessTokenResponseDto requestAccessToken(String oauthToken) {
		String params = UriComponentsBuilder.fromUriString(kakaoOauthProperties.getTokenRequestUrl())
				.queryParam(CODE.key, oauthToken)
				.queryParam(CLIENT_ID.key, kakaoOauthProperties.getClientId())
				.queryParam(CLIENT_SECRET.key, kakaoOauthProperties.getClientSecret())
				.queryParam(REDIRECT_URI.key, kakaoOauthProperties.getRedirectUrl())
				.queryParam(GRANT_TYPE.key, getFixGrantType())
				.toUriString();

		ResponseEntity<String> exchange = new RestTemplate().exchange(
				params,
				HttpMethod.POST,
				null,
				String.class
		);

		return CustomObjectMapper.readValue(exchange.getBody(), KakaoAccessTokenResponseDto.class);
	}

	@Transactional(readOnly = true)
	protected KakaoUserInfoResponseDto requestUserInfo(KakaoAccessTokenResponseDto kakaoAccessTokenResponseDto) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(AUTHORIZATION.key, getFixPrefixJwt() + kakaoAccessTokenResponseDto.getAccessToken());

		ResponseEntity<String> exchange = new RestTemplate().exchange(
				kakaoOauthProperties.getUserInfoRequestUrl(),
				HttpMethod.GET,
				new HttpEntity(httpHeaders),
				String.class
		);

		return CustomObjectMapper.readValue(exchange.getBody(), KakaoUserInfoResponseDto.class);
	}

}
