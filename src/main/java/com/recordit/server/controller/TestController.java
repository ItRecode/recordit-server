package com.recordit.server.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recordit.server.util.SessionUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {
	private final SessionUtil sessionUtil;
	private final HttpServletResponse httpServletResponse;

	@GetMapping("/test")
	public String test() {
		// sessionUtil.saveUserIdInSession(1L);
		httpServletResponse.setHeader(
				"Set-Cookie",
				ResponseCookie.from("hello", "world")
						.secure(true)
						.sameSite("None")
						.domain("record-it-test.netlify.app")
						.path("/")
						.httpOnly(false)
						.build().toString()
		);
		return "굳!";
	}

}
