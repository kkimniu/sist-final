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

    private final JwtUtil jwtUtil;

    @PostMapping("/verify")
    public ResponseEntity<?> validateAccess(@RequestHeader("Authorization") String authorization) {
        String token = jwtUtil.substringToken(authorization);
        if(jwtUtil.validateToken(token)){
            Claims userInfo = jwtUtil.getUserInfoFromToken(token);
            return new ResponseEntity<Claims>(userInfo, HttpStatus.OK);
        }else{
            return new ResponseEntity<ResponseDto>(new ResponseDto("사용자 인증에 실패하였습니다."), HttpStatus.UNAUTHORIZED);
        }
    }
}
