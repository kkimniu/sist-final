package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;

@Component
@RequiredArgsConstructor
public class GameManagerImpl implements GameManager {

    private final GameAdministrationService gameAdministrationService;
    private final int GAME_LIFE_CYCLE = 30;
    private ArrayDeque<Game> games = new ArrayDeque<>();

    @Scheduled(cron = "0 0/10 * * * *")
    @Override
    public void managementGameSessionsLifeCycle() {

        if (!games.isEmpty()) {
            // 현재시간 - 세션시작 시간을 분 단위로 치환한 값
            int minutesBetween = gameAdministrationService.getMinutesBetween(games.peekFirst());

            // 세션의 생명 주기가 끝났으면 선입 세션 삭제
            if (minutesBetween >= GAME_LIFE_CYCLE) {
                // TODO 게임 세션 삭제 전 DB 저장 필요
                games.removeFirst();
                System.out.println("Game Session Closed, Games size: " + games.size());
            }
        }

        // 게임 세션 1개 생성
        games.add(gameAdministrationService.createGame());
        System.out.println("Game Session Created, Games size: " + games.size());
    }

}
