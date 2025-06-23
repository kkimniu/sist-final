package io.cavia.trader.module.game.service;

import io.cavia.trader.module.client.connector.RestWebClientImpl;
import io.cavia.trader.module.client.dto.StocksOutput;
import io.cavia.trader.module.game.service.dto.Game;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class GameAdministrationServiceImpl implements GameAdministrationService {

    private final RestWebClientImpl restWebClient;

    private List<StocksOutput> stocks;
    private ArrayDeque<Game> games;

    @Override
    public void createGame() {
        if (stocks == null) setStocks();
        Game game = new Game();

        game.setStockId((int) (Math.floor(Math.random() * (stocks.size()-1))));

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
    }

    public void setStocks(){
        this.stocks = restWebClient
                .getStocks()
                .block()
                .getOutput();
    }
}
