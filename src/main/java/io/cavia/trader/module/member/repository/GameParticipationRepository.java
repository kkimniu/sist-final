package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.entity.GameParticipation;

import java.util.List;

public interface GameParticipationRepository {
    List<GameParticipation> findByMemberId(Long memberId);
    List<GameParticipation> findByMemberIdWithPaging(Long memberId, int limit, int offset);
    int countByMemberId(Long memberId);
}
