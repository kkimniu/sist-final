package io.cavia.trader.common.controller;

import io.cavia.trader.module.member.dto.UserRankingDto;
import io.cavia.trader.module.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/")
    public String showMainPage() {
        return "main";
    }

    @GetMapping("/api/rankings/cash")
    public ResponseEntity<List<UserRankingDto>> getCashRankings(@RequestParam(defaultValue = "20") Long limit,
                                                                @RequestParam(defaultValue = "0") Long offset) {
        return ResponseEntity.status(200)
                .body(memberService.findAllOrderByCash(limit, offset));
    }

    @GetMapping("/api/rankings/total-score")
    public ResponseEntity<List<UserRankingDto>> getTotalScoreRankings(@RequestParam(defaultValue = "20") Long limit,
                                                                      @RequestParam(defaultValue = "0") Long offset) {
        return ResponseEntity.status(200)
                .body(memberService.findAllOrderByTotalScore(limit, offset));
    }
}
