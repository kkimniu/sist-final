package io.cavia.trader.module.game.service;

import io.cavia.trader.module.client.dto.QuotesOutput;
import io.cavia.trader.module.client.dto.TradesOutput;
import io.cavia.trader.module.game.dto.GameDTO;
import io.cavia.trader.module.game.entity.Member;
import io.jsonwebtoken.Claims;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.List;

public interface GameManager {

    void managementGameSessionsLifeCycle();

    Member getUserInfo(Claims userInfo);

    Deque<GameDTO> getGameDTOs();

    GameDTO addUserToGameAndGetYoungestSession(Claims tokenToClaims, WebSocketSession webSocketSession);

    boolean findChartSessionKeyByUserId(Long userId);

    void replaceChartSessionByUserId(long targetId, WebSocketSession newSession);

    void removeChartSession(WebSocketSession session);

    GameDTO findGameSessionByUserId(Long userId);

    int getQuotesIndexByLateTime(LocalDateTime startedTime, List<QuotesOutput> quotes);
    int getTradesIndexByLateTime(LocalDateTime startedTime, List<TradesOutput> quotes);
}