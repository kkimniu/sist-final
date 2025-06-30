package io.cavia.trader.module.game.controller;

import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.game.dto.response.ResponseDto;
import io.cavia.trader.module.game.service.GameManager;
import io.cavia.trader.module.jwt.JwtUtil;
import io.cavia.trader.module.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game/api")
@RequiredArgsConstructor
public class GameRestController {
    /**
     * 사용자가 게임 페이지에서 보내는 요청들을 처리하는 RestApi입니다
     * validateAccess(String authorizationHeader) 사용자가 토큰을 포함해 요청을 보내면 인증 후 개인 정보가 담긴 Cliaims 객체를 반환합니다.
     */

    private final JwtUtil jwtUtil;
    private final GameManager gameManager;

    @PostMapping("/verify")
    public ResponseEntity<?> validateAccess(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<Member>(userDetails.getMember(), HttpStatus.OK);
    }

    @GetMapping("/stocks-holding")
    public ResponseEntity<?> getStocksHolding(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            long memberId = userDetails.getMember().getId();

            return new ResponseEntity<ResponseDto>(
                    new ResponseDto("" + gameManager.findGameSessionByUserId(memberId)
                            .getGameParticipations().get(memberId).getStocksHolding()), HttpStatus.OK);

        }catch (Exception e){
            throw new RuntimeException("보유 주식 수 조회 실패", e);
        }
    }
}
