package io.cavia.trader.module.game.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cavia.trader.module.client.dto.TradesOutput;
import io.cavia.trader.module.game.dto.Game;
import io.cavia.trader.module.game.service.GameManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChartWebSocketHandler implements WebSocketHandler {

    private final GameManagerImpl gameManager;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Game game = gameManager.games.getLast();

        game.getChartSessions().put(session.getId(), session);


        try {
            List<Long> stockBaseTime = new ArrayList<Long>();
            stockBaseTime.add(game.getTrades()
                    .get(0)
                    .getCreatedAt()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            );

            Thread thread1 = new Thread(() -> {
                game.getTrades().forEach(trades -> {
                    try {
                        if (!session.isOpen()) return;
                        long relTime = trades
                                .getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli();

                        long timeDifference = relTime - stockBaseTime.get(0);
                        stockBaseTime.set(0, relTime);
                        Thread.sleep(timeDifference);


                        String tradesJson = objectMapper.writeValueAsString(trades);
                        // 메시지 전송 부분 동기화
                        synchronized (session) {
                            if(session.isOpen()) session.sendMessage(new TextMessage(tradesJson));
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                });
            });

            List<Long> orderBaseTime = new ArrayList<Long>();
            orderBaseTime.add(game.getQuotes()
                    .get(0)
                    .getCreatedAt()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            );

            Thread thread2 = new Thread(() -> {
                game.getQuotes().forEach(quotes -> {
                    try {
                        if (!session.isOpen()) return;
                        long relTime = quotes
                                .getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli();

                        long timeDifference = relTime - orderBaseTime.get(0);
                        orderBaseTime.set(0, relTime);
                        Thread.sleep(timeDifference);


                        String quotesJson = objectMapper.writeValueAsString(quotes);

                        // 메시지 전송 부분 동기화
                        synchronized (session) {
                            if(session.isOpen()) session.sendMessage(new TextMessage(quotesJson));
                        }

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                });

            });

            thread1.start();
            thread2.start();

        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        for (Game game : gameManager.games) {
            if (game.getChartSessions().containsKey(session.getId())) {
                game.getChartSessions().remove(session.getId());
                break;
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
