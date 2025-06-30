package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.dto.GameParticipationDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GameParticipationMapper {

    List<GameParticipationDto> findByMemberId(Long memberId);
}