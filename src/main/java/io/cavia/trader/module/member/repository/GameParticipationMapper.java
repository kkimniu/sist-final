package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.entity.GameParticipation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GameParticipationMapper {

    List<GameParticipation> findByMemberId(Long memberId);
    List<GameParticipation> findByMemberIdWithPaging(Long memberId, int limit, int offset);
    int countByMemberId(Long memberId);
}