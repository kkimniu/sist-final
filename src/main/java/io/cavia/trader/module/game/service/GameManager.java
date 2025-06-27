package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.GameDTO;
import io.cavia.trader.module.game.entity.Member;
import io.jsonwebtoken.Claims;
import org.springframework.web.socket.WebSocketSession;

import java.util.Deque;

public interface GameManager {

    void managementGameSessionsLifeCycle();
    Member getUserInfo(Claims userInfo);
    Deque<GameDTO> getGameDTOs();
    GameDTO addUserToGameAndGetYoungestSession(Claims tokenToClaims, WebSocketSession webSocketSession);
    Long findChartSessionKeyBySessionId(WebSocketSession targetSession);
    void removeChartSession(Long chartSessionKey);
}
