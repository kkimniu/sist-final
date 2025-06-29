package io.cavia.trader.module.game.service;

import io.cavia.trader.module.client.connector.RestWebClientImpl;
import io.cavia.trader.module.client.dto.StocksOutput;
import io.cavia.trader.module.game.dto.GameDTO;
import io.cavia.trader.module.game.entity.GameParticipation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GameAdministrationServiceImpl implements GameAdministrationService {

    private final RestWebClientImpl restWebClient;

    private List<StocksOutput> stocks;

    @Override
    public GameDTO createGame() {
        if (stocks == null) setStocks();
        GameDTO gameDTO = new GameDTO();

        gameDTO.setStockId(stocks
                .get((int)(Math.random() * stocks.size())
                )
                .getId()
        );

        gameDTO.setTrades(restWebClient
                .getTrades(gameDTO.getStockId())
                .block()
                .getOutput()
        );
        gameDTO.setQuotes(restWebClient
                .getQuotes(gameDTO.getStockId())
                .block()
                .getOutput()
        );

        gameDTO.setGameParticipations(new ArrayList<GameParticipation>());

        gameDTO.setChatSessions(new ConcurrentHashMap<Long, WebSocketSession>());
        gameDTO.setUserIdsInChartSessions(new ConcurrentHashMap<String, Long>());
        gameDTO.setChartSessions(new ConcurrentHashMap<Long, WebSocketSession>());
        gameDTO.setUserIdsInChatSessions(new ConcurrentHashMap<String, Long>());
        gameDTO.setStartedAt(LocalDateTime.now());

        return gameDTO;
    }


    public void setStocks() {
        this.stocks = restWebClient
                .getStocks()
                .block()
                .getOutput();
    }

    public int getMinutesBetween(GameDTO gameDTO) {

        // 현재 시간과 게임 시작 시간의 차이
        int minutesBetween = (int) (
                LocalDateTime.now()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                        - gameDTO.getStartedAt()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli());

        // 시간이 0이면 바로 리턴
        if (minutesBetween == 0) return minutesBetween;

        // 밀리세컨드 단위 오차 제거
        minutesBetween = minutesBetween % (1000 * 60) == 0 ?
                minutesBetween / (1000 * 60)
                : minutesBetween / (1000 * 60) + 1;

        return minutesBetween;
    }
}
