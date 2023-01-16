package com.recordit.server.service.oauth;

import static com.recordit.server.constant.OauthConstants.*;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.recordit.server.constant.LoginType;
import com.recordit.server.dto.member.GoogleAccessTokenResponseDto;
import com.recordit.server.dto.member.GoogleUserInfoResponseDto;
import com.recordit.server.environment.GoogleOauthProperties;
import com.recordit.server.util.CustomObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOauthService implements OauthService {

	private final GoogleOauthProperties googleOauthProperties;

	@Override
	@Transactional(readOnly = true)
	public LoginType getLoginType() {
		return LoginType.GOOGLE;
	}

	@Override
	@Transactional(readOnly = true)
	public String getUserInfoByOauthToken(String oauthToken) {
		GoogleAccessTokenResponseDto googleAccessTokenResponseDto = requestAccessToken(oauthToken);
		GoogleUserInfoResponseDto googleUserInfoResponseDto = requestUserInfo(googleAccessTokenResponseDto);
		return googleUserInfoResponseDto.getSub();
	}

	@Transactional(readOnly = true)
	protected GoogleAccessTokenResponseDto requestAccessToken(String oauthToken) {
		String params = UriComponentsBuilder.fromUriString(googleOauthProperties.getTokenRequestUrl())
				.queryParam(CODE.key, oauthToken)
				.queryParam(CLIENT_ID.key, googleOauthProperties.getClientId())
				.queryParam(CLIENT_SECRET.key, googleOauthProperties.getClientSecret())
				.queryParam(REDIRECT_URI.key, googleOauthProperties.getRedirectUrl())
				.queryParam(GRANT_TYPE.key, getFixGrantType())
				.toUriString();
		log.info("구글 Oauth AccessToken 요청 : {}", params);

		ResponseEntity<String> exchange = new RestTemplate().exchange(
				params,
				HttpMethod.POST,
				null,
				String.class
		);
		log.info("구글 Oauth AccessToken 응답 : {}", exchange);

		return CustomObjectMapper.readValue(exchange.getBody(), GoogleAccessTokenResponseDto.class);
	}

	@Transactional(readOnly = true)
	protected GoogleUserInfoResponseDto requestUserInfo(GoogleAccessTokenResponseDto googleAccessTokenResponseDto) {
		String uri = UriComponentsBuilder.fromUriString(googleOauthProperties.getUserInfoRequestUrl())
				.queryParam(ID_TOKEN.key, googleAccessTokenResponseDto.getIdToken())
				.toUriString();
		log.info("구글 Oauth UserInfo 요청 : {}", uri);

		ResponseEntity<String> exchange = new RestTemplate().exchange(
				uri,
				HttpMethod.GET,
				null,
				String.class
		);
		log.info("구글 Oauth UserInfo 응답 : {}", exchange);

		return CustomObjectMapper.readValue(exchange.getBody(), GoogleUserInfoResponseDto.class);
	}
}
