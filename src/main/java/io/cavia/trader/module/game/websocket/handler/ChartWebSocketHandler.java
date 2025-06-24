package io.cavia.trader.module.game.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cavia.trader.module.game.dto.Game;
import io.cavia.trader.module.game.service.GameManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class ChartWebSocketHandler implements WebSocketHandler {

    private final GameManagerImpl gameManager;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 유저가 연결되었을 때, 가장 젊은 게임 세션에 연결
        Game game = gameManager.games.getLast();
        game.getChartSessions().put(session.getId(), session);

        // 해당 게임 세션에 할당된 집계 데이터를 순차적으로 웹소켓으로 전송
        // TODO 중간에 난입한 유저일 경우 집계테이블에서 이미 지난 부분을 집합으로 먼저 전송하고 나머지 집계테이블을 보내야함
        try {
            AtomicLong stockBaseTime = new AtomicLong(
                    game.getTrades()
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

                        long timeDifference = relTime - stockBaseTime.get();
                        stockBaseTime.set(relTime);
                        Thread.sleep(timeDifference);


                        String tradesJson = objectMapper.writeValueAsString(trades);
                        // 메시지 전송 부분 동기화
                        synchronized (session) {
                            if (session.isOpen()) session.sendMessage(new TextMessage(tradesJson));
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                });
            });

            AtomicLong orderBaseTime = new AtomicLong(
                    game.getQuotes()
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

                        long timeDifference = relTime - orderBaseTime.get();
                        orderBaseTime.set(relTime);
                        Thread.sleep(timeDifference);


                        String quotesJson = objectMapper.writeValueAsString(quotes);

                        // 메시지 전송 부분 동기화
                        synchronized (session) {
                            if (session.isOpen()) session.sendMessage(new TextMessage(quotesJson));
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
    // 유저가 웹소켓 연결을 종료하면 유저가 속한 세션을 찾아서 삭제
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
