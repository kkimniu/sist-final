package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.member.entity.GameParticipation;

public interface GameAdministrationService {

    GameDto createGame();

    GameParticipation getLastGameParticipation(long memberId);
}
