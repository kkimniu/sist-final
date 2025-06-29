package io.cavia.trader.module.member.mapper;

import io.cavia.trader.module.member.dto.GameParticipationDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GameParticipationMapper {

    public List<GameParticipationDto> findByMemberId(int memberId);
}