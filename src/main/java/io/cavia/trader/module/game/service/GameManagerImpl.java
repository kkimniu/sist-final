package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.Game;
import io.cavia.trader.module.game.entity.Member;
import io.cavia.trader.module.game.repository.GameMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
@RequiredArgsConstructor
public class GameManagerImpl implements GameManager {

    private final GameAdministrationService gameAdministrationService;
    private final GameMapper gameMapper;

    private final int GAME_LIFE_CYCLE = 30;
    public Deque<Game> games = new ArrayDeque<>();

    @Scheduled(cron = "1/10 * * * * *")
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

    @Override
    public Deque<Game> getGames() {
        return this.games;
    }

    public Member getUserInfo(Claims userInfo) {
        return gameMapper.findMemberById(
                Integer.parseInt(userInfo.getSubject()
                )
        );
    }
}
