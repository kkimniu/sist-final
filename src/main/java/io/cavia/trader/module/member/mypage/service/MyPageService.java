package io.cavia.trader.module.member.mypage.service;

import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.mypage.dto.GameParticipationDto;

import java.util.List;

public interface MyPageService {

    public List<GameParticipationDto> findByMemberId(int memberId);
    public Member findById(Long id);
}
