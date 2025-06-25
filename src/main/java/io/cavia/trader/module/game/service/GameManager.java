package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.Game;

import java.util.Deque;

public interface GameManager {

    void managementGameSessionsLifeCycle();
    Deque<Game> getGames();
}
