package io.cavia.trader.module.member.mypage.service;

import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.mypage.dto.GameParticipationDto;

import java.time.LocalDateTime;
import java.util.List;

public interface MyPageService {

    public List<GameParticipationDto> findByMemberId(int memberId);

    public Member findById(Long id);

    public void changeNickname(int id, String nickname, LocalDateTime nicknameUpdatedAt);

    public boolean validatePassword(Long id, String password);

    public int changePassword(int id, String password, LocalDateTime passwordUpdatedAt);

    public int resetCash(int id);

    public int deleteMember(Long id, String password);
}
