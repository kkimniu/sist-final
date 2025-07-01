package io.cavia.trader.module.game.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cavia.trader.module.client.dto.QuotesOutput;
import io.cavia.trader.module.client.dto.TradesOutput;
import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.game.service.GameManager;
import io.cavia.trader.module.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class ChartWebSocketHandler implements WebSocketHandler {

    private final GameManager gameManager;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    private GameDto gameDto;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String token = message.getPayload().toString();
        if(jwtUtil.validateToken(token)){
            // 유저가 연결되었을 때, 가장 젊은 게임 세션에 연결 (유저가 이미 세션에 속해 있다면 DTO의 세션만 교체)
            gameDto = gameManager.addChartSessionToGameAndGetYoungestSession(
                    jwtUtil.getUserInfoFromToken(token), session);
            // 게임 입장 처리 완료, 자신이 포함된 게임 참가 인원 수 게임에 참여중인 모든 세션에 전달
            gameDto.getChartSessions().values().forEach(s -> {
            try {
                if (s.isOpen()) s.sendMessage(new TextMessage("numberOfParticipation||"
                        + gameDto.getGameParticipations().size()));
            }catch (Exception e){
                throw new RuntimeException("유저 참여 변동 사항 멀티캐스트 중 예외 발생!", e);
            }
        });


            // 해당 게임 세션에 할당된 집계 데이터를 순차적으로 웹소켓으로 전송
            // TODO 중간에 난입한 유저일 경우 집계테이블에서 이미 지난 부분을 집합으로 먼저 전송하고 나머지 집계테이블을 보내야함
            try {

                // 유저에게 전송해야 할 집계 테이블 인덱스 구하기
                int tradesIdx = gameManager.getTradesIndexByLateTime(gameDto.getStartedAt(), gameDto.getTrades());

                // 인덱스 0부터 tradesIdx까지만 새로운 list로 생성 후 먼저 전송
                List<TradesOutput> previewersTrades = IntStream
                        .range(0, tradesIdx)
                        .mapToObj(gameDto.getTrades()::get)
                        .toList();
                // JAVA16부터는 콜렉터로 래핑할 필요 없이 그냥 toList() 사용 가능!!

                String previewersTradesJson = objectMapper.writeValueAsString(previewersTrades);

                if (session.isOpen()) session.sendMessage(new TextMessage("previewersTrades||" + previewersTradesJson));


                AtomicLong stockBaseTime = new AtomicLong(
                        gameDto.getTrades()
                                .get(tradesIdx)
                                .getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                );

                Thread thread1 = new Thread(() -> {
                        List<TradesOutput> trades = gameDto.getTrades();
                        for(int i=tradesIdx; i<trades.size(); i++) {
                            try {
                                if (!session.isOpen()) return;
                                long relTime = trades
                                        .get(i)
                                        .getCreatedAt()
                                        .atZone(ZoneId.systemDefault())
                                        .toInstant()
                                        .toEpochMilli();

                                long timeDifference = relTime - stockBaseTime.get();
                                stockBaseTime.set(relTime);
                                Thread.sleep(timeDifference);


                                String tradesJson = objectMapper.writeValueAsString(trades.get(i));
                                // 메시지 전송 부분 동기화
                                synchronized (session) {
                                    if (session.isOpen()) session.sendMessage(new TextMessage("trades||" + tradesJson));
                                }
                            } catch (Exception e2) {
                                throw new RuntimeException(e2);
                            }
                        }
                });

                int quotesIdx = gameManager.getQuotesIndexByLateTime(gameDto.getStartedAt(), gameDto.getQuotes());

                List<QuotesOutput> previewersQuotes = IntStream
                        .range(0, quotesIdx)
                        .mapToObj(gameDto.getQuotes()::get)
                        .toList();
                String previewersQuotesJson = objectMapper.writeValueAsString(previewersQuotes);

                if (session.isOpen()) session.sendMessage(new TextMessage("previewersQuotes||" + previewersQuotesJson));

                AtomicLong orderBaseTime = new AtomicLong(
                        gameDto.getQuotes()
                                .get(quotesIdx)
                                .getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                );

                Thread thread2 = new Thread(() -> {
                        try {
                            List<QuotesOutput> quotes = gameDto.getQuotes();
                            for (int i = quotesIdx; i < quotes.size(); i++) {
                                if (!session.isOpen()) return;
                                long relTime = quotes
                                        .get(i)
                                        .getCreatedAt()
                                        .atZone(ZoneId.systemDefault())
                                        .toInstant()
                                        .toEpochMilli();

                                long timeDifference = relTime - orderBaseTime.get();
                                orderBaseTime.set(relTime);
                                Thread.sleep(timeDifference);


                                String quotesJson = objectMapper.writeValueAsString(quotes.get(i));

                                // 메시지 전송 부분 동기화
                                synchronized (session) {
                                    if (session.isOpen()) session.sendMessage(new TextMessage("quotes||" + quotesJson));
                                }
                            }

                        } catch (Exception e2) {
                            throw new RuntimeException(e2);
                        }

                });

                Thread thread3 = new Thread(() -> {
                    long timeLeft = Duration.between(gameDto.getStartedAt(), LocalDateTime.now()).toSeconds();
                    while(timeLeft < 1800) {
                        try {
                            timeLeft = Duration.between(gameDto.getStartedAt(), LocalDateTime.now()).toSeconds();
                            synchronized (session) {
                                if (session.isOpen()) session.sendMessage(new TextMessage("timeLeft||" + (1800 - timeLeft)));
                            }
                            Thread.sleep(1000);
                        } catch(Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                thread1.start();
                thread2.start();
                thread3.start();

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
        throw new RuntimeException("웹소켓 통신 중 예외 발생: " + exception.getMessage(), exception);
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
