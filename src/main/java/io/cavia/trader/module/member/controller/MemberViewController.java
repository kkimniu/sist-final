package io.cavia.trader.module.member.controller;

import io.cavia.trader.module.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberViewController {

    private final MemberService memberService;

    @GetMapping("/me")
    public String myPage() {
        return "members/me/main";
    }

    @GetMapping("/me/nickname")
    public String nicknameUpdate() {
        return "members/me/nickname-change";
    }

    @GetMapping("/me/password")
    public String passwordVerification() {
        return "members/me/password-change";
    }

    @GetMapping("/me/withdraw")
    public String withdraw() {
        return "members/me/withdraw";
    }

}
