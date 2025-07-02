package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.entity.GameParticipation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GameParticipationMybatisRepository implements GameParticipationRepository {

    private final GameParticipationMapper gameParticipationMapper;

    @Override
    public List<GameParticipation> findByMemberId(Long memberId) {
        return gameParticipationMapper.findByMemberId(memberId);
    }
}
