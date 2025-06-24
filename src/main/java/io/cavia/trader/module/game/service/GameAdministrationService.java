package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.Game;

public interface GameAdministrationService {

    Game createGame();
    int getMinutesBetween(Game game);
}
