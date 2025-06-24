package io.cavia.trader.module.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "member/login";
    }

    @GetMapping("/reset-password")
    public String showEmailForm() {
        return "member/reset-password/email";
    }

}
