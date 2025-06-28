package io.cavia.trader.module.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PasswordViewController {

    @GetMapping("/forgot-password")
    public String showPasswordForm() {
        return "member/reset-password";
    }

}
