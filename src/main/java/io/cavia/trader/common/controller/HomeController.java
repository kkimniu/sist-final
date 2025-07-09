package io.cavia.trader.common.controller;

import io.cavia.trader.common.response.ApiResponse;
import io.cavia.trader.common.response.ApiResponses;
import io.cavia.trader.module.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/")
    public String showMainPage() {
        return "main";
    }

    @GetMapping("/api/rankings/cash")
    public ResponseEntity<ApiResponse<?>> getCashRankings(@RequestParam(defaultValue = "10") Long limit,
                                                          @RequestParam(defaultValue = "0") Long offset) {
        return ApiResponses.ok(memberService.findAllOrderByCash(limit, offset));
    }

    @GetMapping("/api/rankings/total-score")
    public ResponseEntity<ApiResponse<?>> getTotalScoreRankings(@RequestParam(defaultValue = "10") Long limit,
                                                                @RequestParam(defaultValue = "0") Long offset) {
        return ApiResponses.ok(memberService.findAllOrderByTotalScore(limit, offset));
    }
}
