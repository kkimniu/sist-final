package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.GameDTO;
import io.cavia.trader.module.game.dto.UserDTO;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private final int GAME_LIFE_CYCLE = 5;
    public Deque<GameDTO> gameDTOs = new ArrayDeque<>();

    @Scheduled(cron = "1/10 * * * * *")
    @Transactional
    @Override
    public void managementGameSessionsLifeCycle() {

        if (!gameDTOs.isEmpty()) {
            // 현재시간 - 세션시작 시간을 분 단위로 치환한 값
            GameDTO gameDTO = gameDTOs.peekFirst();
            int minutesBetween = gameAdministrationService.getMinutesBetween(gameDTO);

            // 세션의 생명 주기가 끝났으면 선입 세션 삭제
            if (minutesBetween >= GAME_LIFE_CYCLE) {
                // TODO 게임 세션 삭제 전 DB 저장 필요
                gameDTO.getGameParticipations().forEach(gameParticipation -> {
                    gameParticipation.setEnteredAt(LocalDateTime.now());
                    gameMapper.saveGameParticipation(gameParticipation);
                });

                gameDTOs.removeFirst();
                System.out.println("Game Session Closed, Games size: " + gameDTOs.size());
            }
        }

        // 게임 세션 1개 생성
        GameDTO gameDTO = gameAdministrationService.createGame();
        gameDTOs.add(gameDTO);
        gameMapper.saveGame(gameDTO.getStockId(),
                gameDTO.getStartedAt());
        gameDTO.setId(gameMapper.findLastGameId());
        System.out.println("Game Session Created, Games size: " + gameDTOs.size());
    }

    public Member getUserInfo(Claims userInfo) {
        return gameMapper.findMemberById(
                Long.parseLong(userInfo.getSubject()
                )
        );
    }

    @Override
    public GameDTO addUserToGameAndGetYoungestSession(Claims tokenToClaims, WebSocketSession webSocketSession) {
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

        if (!gameDTOs.isEmpty()) {
            GameDTO gameDTO = gameDTOs.peekLast();
            List<GameParticipation> gameParticipations = gameDTO.getGameParticipations();
            if (gameParticipations != null) {
                gameParticipations.add(GameParticipation
                        .builder()
                        .gameId(gameDTO.getId())
                        .memberId(member.getId())
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
    public void removeChartSessionBySessionId(WebSocketSession targetSession) {
        try {
            for (GameDTO gameDTO : gameDTOs) {
                Map<String, Long> sessionKeys = gameDTO.getUserIdsInChartSessions();
                if (sessionKeys.containsKey(targetSession.getId())) {
                    gameDTO.getChartSessions().remove(sessionKeys.get(targetSession.getId()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("세션 삭제 중 예외 발생: 해당 세션을 찾을 수 없습니다.", e);
        }
    }

    @Override
    public boolean findChartSessionKeyByUserId(Long userId) {
        for (GameDTO gameDTO : gameDTOs) {
            if (gameDTO.getChartSessions().containsKey(userId)) return true;
        }
        return false;
    }

    @Override
    public void replaceChartSessionByUserId(long targetId, WebSocketSession newSession) {
        try {
            for (GameDTO gameDTO : gameDTOs) {
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
            for (GameDTO gameDTO : gameDTOs) {
                if (gameDTO.getUserIdsInChartSessions()
                        .containsKey(session.getId())) {

                    gameDTO.getChartSessions()
                            .remove(gameDTO.getUserIdsInChartSessions()
                                    .get(session.getId()));

                    gameDTO.getUserIdsInChartSessions()
                            .remove(session.getId());
                    return;
                }
            }
        }catch (Exception e) {
            throw new RuntimeException("세션 삭제 중 예외 발생: 세션을 찾을 수 없습니다.");
        }
    }


    @Override
    public GameDTO findGameSessionByUserId(Long userId) {
        for (GameDTO gameDTO : gameDTOs) {
            for (GameParticipation gameParticipation : gameDTO.getGameParticipations()) {
                if (gameParticipation.getMemberId() == userId) {
                    return gameDTO;
                }
            }
        }
        throw new RuntimeException("세션 조회 중 예외 발생: 세션에서 해당 유저를 찾을 수 없습니다.");
    }


}
