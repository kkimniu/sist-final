package io.cavia.trader.module.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {

    @GetMapping("/")
    public String showMainPage() {
        return "main";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "member/login";
    }

    @GetMapping("/login-checker")
    public String showLoginChecker() {
        return "member/login-checker";
    }

    @GetMapping("/forgot-password")
    public String showPasswordForm() {
        return "member/password-reset";
    }

}
