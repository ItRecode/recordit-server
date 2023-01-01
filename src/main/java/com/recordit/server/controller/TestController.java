package com.recordit.server.controller;

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

	@GetMapping("/test")
	public String test() {
		sessionUtil.saveUserIdInSession(1L);
		return "굳!";
	}

}
