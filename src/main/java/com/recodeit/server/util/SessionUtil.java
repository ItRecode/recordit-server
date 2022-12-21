package com.recodeit.server.util;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.recodeit.server.exception.NotFoundUserInfoInSessionException;

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
			throw new NotFoundUserInfoInSessionException();
		}
		return userId;
	}

	public void removeSession() {
		httpSession.invalidate();
	}
}