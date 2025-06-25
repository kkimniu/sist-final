package io.cavia.trader.module.member.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/mypage")
@RequiredArgsConstructor
public class ViewMyPageController {

    @GetMapping("/mypage-main")
    public String myPage(){
        return "member/mypage/mypage-main";
    }
}
