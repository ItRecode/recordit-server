package com.recodeit.server.controller;

import com.recodeit.server.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/oauth/{logintype}")
    public void oauthLogin(@PathVariable("logintype") String loginType) {
        memberService.oauthLogin(loginType);
    }

    @PostMapping("/member/oauth/{loginType}")
    public void oauthRegister(@PathVariable("logintype") String loginType) {
        memberService.oauthRegister(loginType);
    }
}
