package io.cavia.trader.module.game.controller;

import io.cavia.trader.module.game.dto.response.ResponseDto;
import io.cavia.trader.module.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game/api")
@RequiredArgsConstructor
public class GameRestApi {
    /**
     * 사용자가 게임 페이지에서 보내는 요청들을 처리하는 RestApi입니다
     * validateAccess(String authorizationHeader) 사용자가 토큰을 포함해 요청을 보내면 인증 후 개인 정보가 담긴 Cliaims 객체를 반환합니다.
     */

    private final JwtUtil jwtUtil;

    @PostMapping("/verify")
    public ResponseEntity<?> validateAccess(@RequestHeader("Authorization") String authorizationHeader) {
        String token = jwtUtil.substringToken(authorizationHeader);
        if(jwtUtil.validateToken(token)){
            Claims userInfo = jwtUtil.getUserInfoFromToken(token);
            return new ResponseEntity<Claims>(userInfo, HttpStatus.OK);
        }else{
            return new ResponseEntity<ResponseDto>(new ResponseDto("사용자 인증에 실패하였습니다."), HttpStatus.UNAUTHORIZED);
        }
    }
}
