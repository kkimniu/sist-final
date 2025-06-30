package io.cavia.trader.module.member.controller;

import io.cavia.trader.module.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("member/mypage")
@RequiredArgsConstructor
public class MemberViewController {

    private final MemberService memberService;

    @GetMapping("/mypage-main")
    public String myPage() {
        return "member/mypage/mypage-main";
    }

    @GetMapping("/nickname-edit")
    public String nicknameUpdate() {
        return "member/mypage/nickname-edit";
    }

    @GetMapping("/password-change")
    public String passwordVerification() {
        return "member/mypage/password-change";
    }

    @GetMapping("/withdraw-verification")
    public String withdrawVerification() {
        return "member/mypage/withdraw-verification";
    }

    @GetMapping("/withdraw")
    public String withdrawn() {
        return "member/mypage/withdraw";
    }

    @GetMapping("/mainpage")
    public String mainpage() {
        return "member/mypage/mainpage";
    }

}
