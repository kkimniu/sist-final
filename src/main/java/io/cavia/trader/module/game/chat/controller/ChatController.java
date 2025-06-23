package io.cavia.trader.module.game.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChatController {

    @RequestMapping("/chatText")
    public String chatForm() {
        return "chat/chat";
    }
}
