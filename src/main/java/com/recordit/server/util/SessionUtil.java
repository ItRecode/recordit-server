package com.recordit.server.util;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.recordit.server.exception.NotFoundUserInfoInSessionException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SessionUtil {

	private static final String PREFIX_USER_ID = "LOGIN_USER_ID";
	private final HttpSession httpSession;

	public void saveUserIdInSession(Long id) {
		httpSession.setAttribute(PREFIX_USER_ID, id);
	}

	public Long findUserIdBySession() {
		Long userId = (Long)httpSession.getAttribute(PREFIX_USER_ID);
		if (userId == null) {
			throw new NotFoundUserInfoInSessionException("세션에 사용자 정보가 저장되어 있지 않습니다");
		}
		return userId;
	}

	public void invalidateSession() {
		httpSession.invalidate();
	}
}