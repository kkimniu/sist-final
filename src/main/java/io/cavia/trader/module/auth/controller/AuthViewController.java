package io.cavia.trader.module.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/login-checker")
    public String showLoginChecker() {
        return "auth/login-checker";
    }

    @GetMapping("/forgot-password")
    public String showPasswordForm() {
        return "auth/password-reset";
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "auth/signup";
    }
}
