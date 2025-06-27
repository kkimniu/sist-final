package io.cavia.trader.module.game.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cavia.trader.module.game.dto.GameDTO;
import io.cavia.trader.module.game.service.GameManager;
import io.cavia.trader.module.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class ChartWebSocketHandler implements WebSocketHandler {

    private final GameManager gameManager;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String token = message.getPayload().toString();
        if(jwtUtil.validateToken(token)){
            // 유저가 연결되었을 때, 가장 젊은 게임 세션에 연결 (유저가 이미 세션에 속해 있다면 DTO의 세션만 교체)
            GameDTO gameDTO = gameManager.addUserToGameAndGetYoungestSession(
                    jwtUtil.getUserInfoFromToken(token), session);

            // 해당 게임 세션에 할당된 집계 데이터를 순차적으로 웹소켓으로 전송
            // TODO 중간에 난입한 유저일 경우 집계테이블에서 이미 지난 부분을 집합으로 먼저 전송하고 나머지 집계테이블을 보내야함
            // TODO 각각의 웹소켓 연결 콜백이 순차 실행 주기 사이에 세션 생성 스레드풀이 겹쳐셔 실행 되면
            //  한 유저가 가진 두개 의 세션이 각각 다른 게임에 포함 될 가능성이 있음 이 부분 동기화 하는 로직이 필요함
            try {
                AtomicLong stockBaseTime = new AtomicLong(
                        gameDTO.getTrades()
                                .get(0)
                                .getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                );

                Thread thread1 = new Thread(() -> {
                    gameDTO.getTrades().forEach(trades -> {
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
                            throw new RuntimeException(e2);
                        }
                    });
                });

                AtomicLong orderBaseTime = new AtomicLong(
                        gameDTO.getQuotes()
                                .get(0)
                                .getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                );


                Thread thread2 = new Thread(() -> {
                    gameDTO.getQuotes().forEach(quotes -> {
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
                            throw new RuntimeException(e2);
                        }
                    });

                });

                thread1.start();
                thread2.start();

            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        }else {
            session.sendMessage(new TextMessage("유효하지 않은 JWT 토큰입니다."));
            throw new RuntimeException("유효하지 않은 JWT 토큰을 가진 사용자가 게임 입장을 시도하였습니다 sessionID:" + session.getId());
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    // 유저가 웹소켓 연결을 종료하면 세션을 종료하고 유저가 속한 세션을 찾아서 삭제
        gameManager.removeChartSession(session);
        session.close();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
