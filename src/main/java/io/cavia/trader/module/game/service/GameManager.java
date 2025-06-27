package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.Game;
import io.cavia.trader.module.game.entity.Member;
import io.jsonwebtoken.Claims;

import java.util.Deque;

public interface GameManager {

    void managementGameSessionsLifeCycle();
    Deque<Game> getGames();
    Member getUserInfo(Claims userInfo);
}
