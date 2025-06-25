package io.cavia.trader.module.member.mypage.controller;

import io.cavia.trader.module.member.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("member/mypage")
@RequiredArgsConstructor
public class ViewMyPageController {

    private final MyPageService myPageService;

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
    public String passwordVerification(@RequestParam int id, @RequestParam String password, Model model) {
        model.addAttribute("id", id);
        if (!myPageService.validateDuplicatePassword(id, password)) {
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
        int result = myPageService.changePassword(id, password, LocalDateTime.now());
        if (result <= 0) {
            model.addAttribute("id", id);
            model.addAttribute("errorMessage", "비밀번호 변경이 실패했습니다.");
            return "member/mypage/password-change.html";
        }
        return "member/mypage/mypage-main.html";
    }

}
