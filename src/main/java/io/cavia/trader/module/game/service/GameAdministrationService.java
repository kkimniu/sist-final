package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.GameDto;

public interface GameAdministrationService {

    GameDto createGame();
    int getMinutesBetween(GameDto gameDTO);
}
