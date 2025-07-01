package io.cavia.trader.module.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "members/login";
    }

    @GetMapping("/login-checker")
    public String showLoginChecker() {
        return "members/login-checker";
    }

    @GetMapping("/forgot-password")
    public String showPasswordForm() {
        return "members/password-reset";
    }

}
