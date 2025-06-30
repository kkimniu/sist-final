package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.dto.GameParticipationDto;

import java.util.List;

public interface GameParticipationRepository {
    List<GameParticipationDto> findByMemberId(int memberId);
}
