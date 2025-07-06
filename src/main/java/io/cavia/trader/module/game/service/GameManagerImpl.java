package io.cavia.trader.module.game.service;

import io.cavia.trader.module.client.dto.QuotesOutput;
import io.cavia.trader.module.client.dto.TradesOutput;
import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.game.dto.GameSessionDto;
import io.cavia.trader.module.game.dto.OrderDto;
import io.cavia.trader.module.game.dto.PlayerStatusDto;
import io.cavia.trader.module.game.dto.event.GameCompleted;
import io.cavia.trader.module.game.repository.GameRepository;
import io.cavia.trader.module.member.entity.GameParticipation;
import io.cavia.trader.module.member.entity.Member;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
@Getter
public class GameManagerImpl implements GameManager {

    private final GameAdministrationService gameAdministrationService;
    private final GameRepository gameRepository;
    private final GameSessionDto gameSessionDto;
    private final ScoreUtil scoreUtil;
    private final ApplicationEventPublisher eventPublisher;

    private final int GAME_LIFE_CYCLE = 1000 * 60 * 1;

    //@Scheduled(cron = "0 */10 * * * *")
    @Scheduled(cron = "*/10 * * * * *")
    @Transactional
    @Override
    public void managementGameSessionsLifeCycle() {

        if (!gameSessionDto.getGameDtos().isEmpty()) {
            // 현재시간 - 세션시작 시간을 분 단위로 치환한 값
            GameDto gameDto = gameSessionDto.getGameDtos().peekFirst();
            long timesBetween = Duration.between(gameDto.getStartedAt(), LocalDateTime.now()).toMillis();

            // 세션의 생명 주기가 끝났으면 선입 세션 삭제
            if (timesBetween >= GAME_LIFE_CYCLE) {

                if (!gameDto.getPlayerStatusDtos().isEmpty()) {
                    // TODO 게임 세션 삭제 전 DB 저장 필요(game 객체는 세션 만들어 질때 저장 했음)
                    Map<Long, PlayerStatusDto> playersSortedByRank = scoreUtil.evaluatePlayers(
                            gameDto.getPlayerStatusDtos(), gameDto.getTrades().get(gameDto.getTrades().size() - 1).getStckPrpr());

                    // DB 저장
                    playersSortedByRank.values().forEach(playerStatusDto -> {
                        gameRepository.saveGameParticipation(
                                GameParticipation.builder()
                                        .gameId(playerStatusDto.getGameId())
                                        .memberId(playerStatusDto.getMemberId())
                                        .gameRank(playerStatusDto.getGameRank())
                                        .postCash(playerStatusDto.getPostCash())
                                        .earnedCash(playerStatusDto.getPostCash() - playerStatusDto.getEarnedCash())
                                        .postScore(playerStatusDto.getPostScore())
                                        .earnedScore(playerStatusDto.getPostScore() - playerStatusDto.getEarnedScore())
                                        .returnRate(playerStatusDto.getReturnRate())
                                        .enteredAt(LocalDateTime.now())
                                        .build());

                        gameRepository.updateCashAndTotalScoreById(playerStatusDto.getMemberId(),
                                playerStatusDto.getEarnedCash(),
                                playerStatusDto.getEarnedScore()
                        );
                    });
                }
                // 게임 종료 후 DB 저장 후 세션 저장 완료 알림과 메모리 클리어를 이벤트에서 처리하기 위해 호출
                eventPublisher.publishEvent(new GameCompleted(gameDto));
            }
        }
        // 게임 세션 1개 생성
        GameDto gameDto = gameAdministrationService.createGame();
        gameSessionDto.getGameDtos().add(gameDto);
        gameRepository.saveGame(gameDto.getStockId(),
                gameDto.getStartedAt());
        gameDto.setId(gameRepository.findLastGameId());
        log.debug("Game Session Created, Games size: {}", gameSessionDto.getGameDtos().size());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void sendGameEndProcessingCompleted(GameCompleted event) {
        // 이 이벤트 리스너는 DB 커밋 종료 직후에 호출됨
        event.getGameDto().getChartSessions().values().forEach(session -> {
            synchronized (session) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage("isProsseced||"));
                    } catch (IOException e) {
                        throw new RuntimeException("게임 종료 처리 알림 메세지 송신 중 예외 발생", e);
                    }
                }
            }
        });

        event.getGameDto().getChartSessions().values().forEach(chartSessions -> {
            try {
                chartSessions.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        event.getGameDto().getChatSessions().values().forEach(chatSessions -> {
            try {
                chatSessions.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        gameSessionDto.getGameDtos().removeFirst();
        log.debug("Game Session Closed, Games size: {}", gameSessionDto.getGameDtos().size());
    }

    @Override
    public Member getUserInfo(Claims userInfo) {
        return gameRepository.findMemberById(
                Long.parseLong(userInfo.getSubject()
                )
        );
    }

    @Override
    public GameDto addChartSessionToGameAndGetYoungestSession(Claims tokenToClaims, WebSocketSession webSocketSession) {
        /**
         * 인자로 받은 Cliaims로 유저 정보를 조회해서 GameSession에 유저 정보를 주입하는 메서드 입니다.
         * 조회 시점의 가장 젊은 세션에 유저 정보를 주입하기 때문에 정합성을 위해
         * 유저 정보 주입과 주입 세션 반환을 한 메서드에서 처리하였습니다
         */
        io.cavia.trader.module.member.entity.Member member = getUserInfo(tokenToClaims);

        // 이미 참여중인 유저는 웹소켓 세션만 갈아 끼우고 연결
        if (findChartSessionKeyByUserId(member.getId())) {
            replaceChartSessionByUserId(member.getId(), webSocketSession);
            return findGameSessionByUserId(member.getId());
        }

        if (!gameSessionDto.getGameDtos().isEmpty()) {

            GameDto gameDto = gameSessionDto.getGameDtos().peekLast();

            Map<Long, PlayerStatusDto> playerStatusDtos = gameDto.getPlayerStatusDtos();
            if (playerStatusDtos != null) {
                playerStatusDtos.put(member.getId(), PlayerStatusDto.builder()
                        .gameId(gameDto.getId())
                        .memberId(member.getId())
                        .memberNickname(member.getNickname())
                        .stocksHolding(0)
                        .gameRank(member.getTotalScore())
                        .postCash(member.getCash())
                        .earnedCash(member.getCash())
                        .postScore(member.getTotalScore())
                        .earnedScore(member.getTotalScore())
                        .orderDto(OrderDto.builder().build())
                        .returnRate(new BigDecimal(0))
                        .build()
                );
            }
            synchronized (gameDto.getChartSessions()) {
                gameDto.getChartSessions().put(member.getId(), webSocketSession);
            }
            synchronized (gameDto.getUserIdsInChartSessions()) {
                gameDto.getUserIdsInChartSessions().put(webSocketSession.getId(), member.getId());
            }

            return gameDto;

        } else {
            throw new RuntimeException("현재 생성된 게임 세션이 존재하지 않습니다.");
        }
    }

    @Override
    public GameDto addChatSessionToGameAndGetYoungestSession(Claims tokenToClaims, WebSocketSession webSocketSession) {
        /**
         * 인자로 받은 Cliaims로 유저 정보를 조회해서 GameSession에 유저 정보를 주입하는 메서드 입니다.
         * 조회 시점의 가장 젊은 세션에 유저 정보를 주입하기 때문에 정합성을 위해
         * 유저 정보 주입과 주입 세션 반환을 한 메서드에서 처리하였습니다
         */
        io.cavia.trader.module.member.entity.Member member = getUserInfo(tokenToClaims);

        // 이미 참여중인 유저는 웹소켓 세션만 갈아 끼우고 연결
        if (findChatSessionKeyByUserId(member.getId())) {
            replaceChatSessionByUserId(member.getId(), webSocketSession);
            return findGameSessionByUserId(member.getId());
        }

        if (!gameSessionDto.getGameDtos().isEmpty()) {
            GameDto gameDto = gameSessionDto.getGameDtos().peekLast();
            synchronized (gameDto.getChatSessions()) {
                gameDto.getChatSessions().put(member.getId(), webSocketSession);
            }
            synchronized (gameDto.getUserIdsInChatSessions()) {
                gameDto.getUserIdsInChatSessions().put(webSocketSession.getId(), member.getId());
            }
            return gameDto;

        } else {
            throw new RuntimeException("현재 생성된 게임 세션이 존재하지 않습니다.");
        }
    }

    @Override
    public boolean findChartSessionKeyByUserId(Long memberId) {
        for (GameDto gameDto : gameSessionDto.getGameDtos()) {
            if (gameDto.getChartSessions().containsKey(memberId)) return true;
        }
        return false;
    }

    @Override
    public boolean findChatSessionKeyByUserId(Long memberId) {
        for (GameDto gameDto : gameSessionDto.getGameDtos()) {
            if (gameDto.getChatSessions().containsKey(memberId)) return true;
        }
        return false;
    }

    @Override
    public GameDto findGameSessionByUserId(Long memberId) {
        for (GameDto gameDto : gameSessionDto.getGameDtos()) {
            if (gameDto.getPlayerStatusDtos().containsKey(memberId)) {
                return gameDto;
            }
        }
        throw new RuntimeException("세션 조회 중 예외 발생: 세션에서 해당 유저를 찾을 수 없습니다.");
    }


    @Override
    public void replaceChartSessionByUserId(long targetId, WebSocketSession newSession) {
        try {
            for (GameDto gameDto : gameSessionDto.getGameDtos()) {
                synchronized (gameDto.getChartSessions()) {
                    if (gameDto.getChartSessions().containsKey(targetId)) {
                        gameDto.getChartSessions().get(targetId).close();
                        gameDto.getChartSessions().replace(targetId, newSession);
                        gameDto.getUserIdsInChartSessions().put(newSession.getId(), targetId);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("세션 교체 중 예외 발생: 세션에서 해당 유저를 찾을 수 없습니다.", e);
        }
    }

    @Override
    public void replaceChatSessionByUserId(long targetId, WebSocketSession newSession) {
        try {
            for (GameDto gameDto : gameSessionDto.getGameDtos()) {
                synchronized (gameDto.getChatSessions()) {
                    if (gameDto.getChatSessions().containsKey(targetId)) {
                        gameDto.getChatSessions().get(targetId).close();
                        gameDto.getChatSessions().replace(targetId, newSession);
                        gameDto.getUserIdsInChatSessions().put(newSession.getId(), targetId);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("세션 교체 중 예외 발생: 세션에서 해당 유저를 찾을 수 없습니다.", e);
        }
    }

    @Override
    public void removeChartSession(WebSocketSession session) {
        try {
            for (GameDto gameDto : gameSessionDto.getGameDtos()) {
                if (gameDto.getUserIdsInChartSessions()
                        .containsKey(session.getId())) {

                    long targetId = gameDto.getUserIdsInChartSessions()
                            .get(session.getId());

                    gameDto.getUserIdsInChartSessions()
                            .remove(session.getId());
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("세션 삭제 중 예외 발생: 세션을 찾을 수 없습니다.", e);
        }
    }

    @Override
    public void removeChatSession(WebSocketSession session) {
        try {
            for (GameDto gameDto : gameSessionDto.getGameDtos()) {
                if (gameDto.getUserIdsInChatSessions()
                        .containsKey(session.getId())) {

                    long targetId = gameDto.getUserIdsInChatSessions()
                            .get(session.getId());

                    gameDto.getUserIdsInChatSessions()
                            .remove(session.getId());
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("세션 삭제 중 예외 발생: 세션을 찾을 수 없습니다.", e);
        }
    }

    @Override
    public int getQuotesIndexByLateTime(LocalDateTime startedTime, List<QuotesOutput> quotes) {

        long lateTime = Duration.between(startedTime, LocalDateTime.now()).toMillis();

        int Index = 0;
        long comparisonTime = 0;
        long chartStartTime = quotes
                .get(Index++)
                .getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        while (lateTime > comparisonTime - chartStartTime) {
            comparisonTime = quotes
                    .get(Index++)
                    .getCreatedAt()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();
        }

        return Index;
    }

    @Override
    public int getTradesIndexByLateTime(LocalDateTime startedTime, List<TradesOutput> trades) {

        long lateTime = Duration.between(startedTime, LocalDateTime.now()).toMillis();

        int index = 0;
        long comparisonTime = 0;
        long chartStartTime = trades
                .get(0)
                .getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        while (lateTime > comparisonTime - chartStartTime) {
            comparisonTime = trades
                    .get(index++)
                    .getCreatedAt()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();
        }

        return index;
    }

}
