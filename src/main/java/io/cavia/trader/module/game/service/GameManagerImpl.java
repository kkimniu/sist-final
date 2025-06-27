package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.GameDTO;
import io.cavia.trader.module.game.dto.UserDTO;
import io.cavia.trader.module.game.entity.Member;
import io.cavia.trader.module.game.repository.GameMapper;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.math.BigDecimal;
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

    private final int GAME_LIFE_CYCLE = 30;
    public Deque<GameDTO> gameDTOs = new ArrayDeque<>();

    @Scheduled(cron = "1/10 * * * * *")
    @Override
    public void managementGameSessionsLifeCycle() {

        if (!gameDTOs.isEmpty()) {
            // 현재시간 - 세션시작 시간을 분 단위로 치환한 값
            int minutesBetween = gameAdministrationService.getMinutesBetween(gameDTOs.peekFirst());

            // 세션의 생명 주기가 끝났으면 선입 세션 삭제
            if (minutesBetween >= GAME_LIFE_CYCLE) {
                // TODO 게임 세션 삭제 전 DB 저장 필요
                gameDTOs.removeFirst();
                System.out.println("Game Session Closed, Games size: " + gameDTOs.size());
            }
        }

        // 게임 세션 1개 생성
        gameDTOs.add(gameAdministrationService.createGame());
        System.out.println("Game Session Created, Games size: " + gameDTOs.size());
    }

    public Member getUserInfo(Claims userInfo) {
        return gameMapper.findMemberById(
                Integer.parseInt(userInfo.getSubject()
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
        if (gameDTOs.isEmpty()) {
            GameDTO gameDTO = gameDTOs.peekFirst();
            List<UserDTO> userDTOs = gameDTO.getUserDTOs();
            if (userDTOs != null) {
                userDTOs.add(UserDTO
                        .builder()
                        .userId(member.getId())
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
            return gameDTO;

        } else {
            throw new RuntimeException("현재 생성된 게임 세션이 존재하지 않습니다.");
        }
    }

    @Override
    public Long findChartSessionKeyBySessionId(WebSocketSession targetSession) {
        for (GameDTO gameDTO : gameDTOs) {
            for (Map.Entry<Long, WebSocketSession> entry : gameDTO.getChartSessions().entrySet()) {
                if (targetSession.equals(entry.getValue())) return entry.getKey();
            }
        }
        throw new RuntimeException("해당 유저가 게임에 참가하지 않았습니다.");
    }

    @Override
    public void removeChartSession(Long sessionKey) {
        for (GameDTO gameDTO : gameDTOs) {
            gameDTO.getChartSessions().remove(sessionKey);
        }
    }
}
