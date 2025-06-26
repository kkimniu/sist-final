package io.cavia.trader.module.member.mypage.mapper;

import io.cavia.trader.module.member.mypage.dto.GameParticipationDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyPageMapper {

    public List<GameParticipationDto> findByMemberId(int memberId);
    public int deleteGameParticipation(int memberId);
}
