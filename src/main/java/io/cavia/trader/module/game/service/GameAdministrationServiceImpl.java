package io.cavia.trader.module.game.service;

import io.cavia.trader.module.client.connector.RestWebClientImpl;
import io.cavia.trader.module.client.dto.StocksOutput;
import io.cavia.trader.module.game.dto.ChatLog;
import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.game.dto.PlayerStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
public class GameAdministrationServiceImpl implements GameAdministrationService {

    private final RestWebClientImpl restWebClient;

    private List<StocksOutput> stocks;

    @Override
    public GameDto createGame() {
        if (stocks == null) setStocks();
        GameDto gameDTO = new GameDto();

        gameDTO.setStockId(stocks
                .get((int)(Math.random() * stocks.size()))
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

        gameDTO.setPlayerStatusDtos(new ConcurrentHashMap<Long, PlayerStatusDto>());
        gameDTO.setChatSessions(new ConcurrentHashMap<Long, WebSocketSession>());
        gameDTO.setUserIdsInChartSessions(new ConcurrentHashMap<String, Long>());
        gameDTO.setChartSessions(new ConcurrentHashMap<Long, WebSocketSession>());
        gameDTO.setUserIdsInChatSessions(new ConcurrentHashMap<String, Long>());
        gameDTO.setChatLogs(new ConcurrentLinkedQueue<ChatLog>());
        gameDTO.setStartedAt(LocalDateTime.now());

        return gameDTO;
    }


    public void setStocks() {
        this.stocks = restWebClient
                .getStocks()
                .block()
                .getOutput();
    }

}
