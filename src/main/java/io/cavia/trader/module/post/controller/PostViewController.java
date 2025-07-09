package io.cavia.trader.module.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostViewController {

    @GetMapping("/posts")
    public String showPosts() {
        return "posts/main";
    }

}
