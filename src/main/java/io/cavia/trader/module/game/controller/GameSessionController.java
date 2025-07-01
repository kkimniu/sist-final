package io.cavia.trader.module.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameSessionController {

    @RequestMapping("")
    public String testDetailPage() {
        return "game/main";
    }
}
