package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.GameDTO;

public interface GameAdministrationService {

    GameDTO createGame();
    int getMinutesBetween(GameDTO gameDTO);
}
