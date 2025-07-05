package io.cavia.trader.module.game.service;

import io.cavia.trader.module.client.dto.QuotesOutput;
import io.cavia.trader.module.client.dto.TradesOutput;
import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.game.dto.event.GameCompleted;
import io.cavia.trader.module.member.entity.Member;
import io.jsonwebtoken.Claims;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface GameManager {

    void managementGameSessionsLifeCycle();

    void sendGameEndProcessingCompleted(GameCompleted gameCompleted);

    Member getUserInfo(Claims userInfo);

    GameDto addChartSessionToGameAndGetYoungestSession(Claims tokenToClaims, WebSocketSession webSocketSession);

    GameDto addChatSessionToGameAndGetYoungestSession(Claims tokenToClaims, WebSocketSession webSocketSession);

    boolean findChartSessionKeyByUserId(Long userId);

    boolean findChatSessionKeyByUserId(Long userId);

    void replaceChartSessionByUserId(long targetId, WebSocketSession newSession);

    void replaceChatSessionByUserId(long targetId, WebSocketSession newSession);

    void removeChartSession(WebSocketSession session);

    void removeChatSession(WebSocketSession session);

    GameDto findGameSessionByUserId(Long userId);

    int getQuotesIndexByLateTime(LocalDateTime startedTime, List<QuotesOutput> quotes);

    int getTradesIndexByLateTime(LocalDateTime startedTime, List<TradesOutput> quotes);
}