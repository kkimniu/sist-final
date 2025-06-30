package io.cavia.trader.module.game.service;

import io.cavia.trader.module.client.dto.QuotesOutput;
import io.cavia.trader.module.client.dto.TradesOutput;
import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.game.entity.GameParticipation;
import io.cavia.trader.module.game.entity.Member;
import io.cavia.trader.module.game.repository.GameMapper;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class GameManagerImpl implements GameManager {

    private final GameAdministrationService gameAdministrationService;
    private final GameMapper gameMapper;

    private final int GAME_LIFE_CYCLE = 2;
    public Deque<GameDto> gameDtos = new ArrayDeque<>();

    //@Scheduled(cron = "0 */10 * * * *")
    @Scheduled(cron = "*/10 * * * * *")
    @Transactional
    @Override
    public void managementGameSessionsLifeCycle() {

        if (!gameDtos.isEmpty()) {
            // 현재시간 - 세션시작 시간을 분 단위로 치환한 값
            GameDto gameDTO = gameDtos.peekFirst();
            int minutesBetween = gameAdministrationService.getMinutesBetween(gameDTO);

            // 세션의 생명 주기가 끝났으면 선입 세션 삭제
            if (minutesBetween >= GAME_LIFE_CYCLE) {
                // TODO 게임 세션 삭제 전 DB 저장 필요(game 객체는 세션 만들어 질때 저장 했음)
                gameDTO.getGameParticipations().forEach((memberId, gameParticipation) -> {
                    gameParticipation.setEnteredAt(LocalDateTime.now());
                    gameMapper.saveGameParticipation(gameParticipation);
                });


                gameDTO.getChartSessions().forEach((userId, chartSessions) -> {
                    try {
                        chartSessions.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });


                gameDtos.removeFirst();
                System.out.println("Game Session Closed, Games size: " + gameDtos.size());
            }
        }

        // 게임 세션 1개 생성
        GameDto gameDTO = gameAdministrationService.createGame();
        gameDtos.add(gameDTO);
        gameMapper.saveGame(gameDTO.getStockId(),
                gameDTO.getStartedAt());
        gameDTO.setId(gameMapper.findLastGameId());
        System.out.println("Game Session Created, Games size: " + gameDtos.size());
    }

    public Member getUserInfo(Claims userInfo) {
        return gameMapper.findMemberById(
                Long.parseLong(userInfo.getSubject()
                )
        );
    }

    @Override
    public GameDto addUserToGameAndGetYoungestSession(Claims tokenToClaims, WebSocketSession webSocketSession) {
        /**
         * 인자로 받은 Cliaims로 유저 정보를 조회해서 GameSession에 유저 정보를 주입하는 메서드 입니다.
         * 조회 시점의 가장 젊은 세션에 유저 정보를 주입하기 때문에 정합성을 위해
         * 유저 정보 주입과 주입 세션 반환을 한 메서드에서 처리하였습니다
         */
        Member member = getUserInfo(tokenToClaims);


        // 이미 참여중인 유저는 웹소켓 세션만 갈아 끼우고 연결
        if (findChartSessionKeyByUserId(member.getId())) {
            replaceChartSessionByUserId(member.getId(), webSocketSession);
            return findGameSessionByUserId(member.getId());
        }

        if (!gameDtos.isEmpty()) {
            GameDto gameDTO = gameDtos.peekLast();

            Map<Long, GameParticipation> gameParticipations = gameDTO.getGameParticipations();
            if (gameParticipations != null) {
                gameParticipations.put(member.getId(), GameParticipation.builder()
                        .gameId(gameDTO.getId())
                        .memberId(member.getId())
                        .stocksHolding(0)
                        .gameRank(member.getTotalScore())
                        .postCash(member.getCash())
                        .earnedCash(member.getCash())
                        .postScore(member.getTotalScore())
                        .earnedScore(member.getTotalScore())
                        .returnRate(new BigDecimal(0))
                        .build()
                );
            }
            gameDTO.getChartSessions().put(member.getId(), webSocketSession);
            gameDTO.getUserIdsInChartSessions().put(webSocketSession.getId(), member.getId());

            return gameDTO;

        } else {
            throw new RuntimeException("현재 생성된 게임 세션이 존재하지 않습니다.");
        }
    }

    @Override
    public boolean findChartSessionKeyByUserId(Long memberId) {
        for (GameDto gameDTO : gameDtos) {
            if (gameDTO.getChartSessions().containsKey(memberId)) return true;
        }
        return false;
    }

    @Override
    public GameDto findGameSessionByUserId(Long memberId) {
        for (GameDto gameDTO : gameDtos) {
            if (gameDTO.getGameParticipations().containsKey(memberId)){
                return gameDTO;
            }
        }
        throw new RuntimeException("세션 조회 중 예외 발생: 세션에서 해당 유저를 찾을 수 없습니다.");
    }


    @Override
    public void replaceChartSessionByUserId(long targetId, WebSocketSession newSession) {
        try {
            for (GameDto gameDTO : gameDtos) {
                if (gameDTO.getChartSessions().containsKey(targetId)) {
                    gameDTO.getChartSessions().replace(targetId, newSession);
                    gameDTO.getUserIdsInChartSessions().put(newSession.getId(), targetId);
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("세션 교체 중 예외 발생: 세션에서 해당 유저를 찾을 수 없습니다.", e);
        }
    }

    @Override
    public void removeChartSession(WebSocketSession session) {
        try {
            for (GameDto gameDTO : gameDtos) {
                if (gameDTO.getUserIdsInChartSessions()
                        .containsKey(session.getId())) {

                    long targetId = gameDTO.getUserIdsInChartSessions()
                            .get(session.getId());

                    gameDTO.getUserIdsInChartSessions()
                            .remove(session.getId());

                    // 여기에서 계속 널포인트 발생하는데 이유는 모르겠음
                    gameDTO.getChartSessions()
                            .replace(targetId, null);

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
