package io.cavia.trader.module.game.service;

import io.cavia.trader.module.client.connector.RestWebClientImpl;
import io.cavia.trader.module.client.dto.StocksOutput;
import io.cavia.trader.module.game.dto.Game;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GameAdministrationServiceImpl implements GameAdministrationService {

    private final RestWebClientImpl restWebClient;

    private List<StocksOutput> stocks;

    @Override
    public Game createGame() {
        if (stocks == null) setStocks();
        Game game = new Game();

        game.setStockId(stocks
                .get((int) (Math.floor(Math.random() * (stocks.size() - 1))))
                .getId()
        );

        game.setTrades(restWebClient
                .getTrades(game.getStockId())
                .block()
                .getOutput()
        );

        game.setQuotes(restWebClient
                .getQuotes(game.getStockId())
                .block()
                .getOutput()
        );

        game.setChatSessions(new ConcurrentHashMap<Integer, Session>());
        game.setChartSessions(new ConcurrentHashMap<Integer, Session>());
        game.setStartedAt(LocalDateTime.now());

        return game;
    }


    public void setStocks() {
        this.stocks = restWebClient
                .getStocks()
                .block()
                .getOutput();
    }

    public int getMinutesBetween(Game game) {

        // 현재 시간과 게임 시작 시간의 차이
        int MinutesBetween = (int) (
                LocalDateTime.now()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                        - game.getStartedAt()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli());

        // 시간이 0이면 바로 리턴
        if (MinutesBetween == 0) return MinutesBetween;

        // 밀리세컨드 단위 오차 제거
        MinutesBetween = MinutesBetween % (1000 * 60) == 0 ?
                MinutesBetween / (1000 * 60)
                : MinutesBetween / (1000 * 60) + 1;

        return MinutesBetween;
    }
}
