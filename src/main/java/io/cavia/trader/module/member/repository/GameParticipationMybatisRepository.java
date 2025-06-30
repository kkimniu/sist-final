package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.dto.GameParticipationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GameParticipationMybatisRepository implements GameParticipationRepository {

    private final GameParticipationMapper gameParticipationMapper;

    @Override
    public List<GameParticipationDto> findByMemberId(int memberId) {
        return gameParticipationMapper.findByMemberId(memberId);
    }
}
