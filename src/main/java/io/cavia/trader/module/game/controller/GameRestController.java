package io.cavia.trader.module.game.controller;

import io.cavia.trader.module.game.dto.response.ResponseDto;
import io.cavia.trader.module.game.entity.Member;
import io.cavia.trader.module.game.service.GameManager;
import io.cavia.trader.module.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> validateAccess(@RequestHeader("Authorization") String authorizationHeader) {
        String token = jwtUtil.substringToken(authorizationHeader);
        if(jwtUtil.validateToken(token)){
           return new ResponseEntity<Member>(
                   gameManager.getUserInfo(
                           jwtUtil.getUserInfoFromToken(token)
                   ),
                   HttpStatus.OK);
        }else{
            return new ResponseEntity<ResponseDto>(
                    new ResponseDto("사용자 인증에 실패하였습니다."),
                    HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/stocksHolding")
    public ResponseEntity<?> getStocksHolding(@RequestHeader("Authorization") String authorizationHeader) {
        String token = jwtUtil.substringToken(authorizationHeader);
        if(jwtUtil.validateToken(token)){
            long memberId = gameManager.getUserInfo(
                            jwtUtil.getUserInfoFromToken(
                                    token)).getId();

        return new ResponseEntity<ResponseDto>(
                new ResponseDto(""+gameManager.findGameSessionByUserId(memberId)
                        .getGameParticipations()
                        .get(memberId)
                        .getStocksHolding()), HttpStatus.OK);
        }else{
            return new ResponseEntity<ResponseDto>(
                    new ResponseDto("사용자 인증에 실패하였습니다."),
                    HttpStatus.UNAUTHORIZED);
        }
    }
}
