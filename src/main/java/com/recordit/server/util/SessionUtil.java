package com.recordit.server.util;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.recordit.server.exception.member.NotFoundUserInfoInSessionException;
import com.recordit.server.exception.member.SessionAuthenticationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionUtil {

	private static final String PREFIX_USER_ID = "LOGIN_USER_ID";
	private final HttpSession httpSession;

	public void saveUserIdInSession(Long id) {
		httpSession.setAttribute(PREFIX_USER_ID, id);
	}

	public Long findUserIdBySession() {
		Integer userId = (Integer)httpSession.getAttribute(PREFIX_USER_ID);
		if (userId == null) {
			log.info("세션에 사용자 정보가 저장되어 있지 않습니다");
			invalidateSession();
			throw new NotFoundUserInfoInSessionException("세션에 사용자 정보가 저장되어 있지 않습니다");
		}
		return userId.longValue();
	}

	public void isCorrectSession() {
		if (httpSession.getAttribute(PREFIX_USER_ID) == null) {
			log.info("로그인이 되어있지 않아 접근이 거부되었습니다.");
			invalidateSession();
			throw new SessionAuthenticationException("로그인이 되어있지 않아 접근이 거부되었습니다.");
		}
	}

	public void invalidateSession() {
		httpSession.invalidate();
	}
}