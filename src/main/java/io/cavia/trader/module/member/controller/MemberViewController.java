package io.cavia.trader.module.member.controller;

import io.cavia.trader.module.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/password-verification")
    public String passwordVerification() {
        return "member/mypage/password-verification";
    }

    @PostMapping("/password-verification")
    public String passwordVerification(@RequestParam Long id, @RequestParam String password, Model model) {
        model.addAttribute("id", id);
        if (!memberService.validatePassword(id, password)) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "member/mypage/password-verification.html";
        }
        return "member/mypage/password-change.html";
    }

    @PostMapping("password-change")
    public String passwordChange(@RequestParam int id, @RequestParam String password, @RequestParam String password2, Model model) {
        if (!password.equals(password2)) {
            model.addAttribute("id", id);
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "member/mypage/password-change.html";
        }
        try {
            memberService.changePassword((long) id, password);
        } catch (RuntimeException e) {
            model.addAttribute("id", id);
            model.addAttribute("errorMessage", e.getMessage());
            return "member/mypage/password-change.html";
        }
        return "member/mypage/mypage-main.html";
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
