package io.cavia.trader.module.member.service;

import io.cavia.trader.module.member.dto.GameParticipationDto;
import io.cavia.trader.module.member.dto.UserRankingDto;
import io.cavia.trader.module.member.dto.PasswordChangeRequestDto;
import io.cavia.trader.module.member.entity.Member;

import java.util.List;

public interface MemberService {

    List<GameParticipationDto> getGameParticipationByMemberId(Long memberId);

    Member getMemberById(Long id);

    Member getMemberByEmail(String email);

    void changeNickname(Long id, String nickname);

    void validatePassword(Long id, String password);

    void changePassword(Long id, PasswordChangeRequestDto requestDto);

    void resetCash(Long id);

    void withdrawMember(Long id, String password);

    boolean isMemberByEmail(String email);

    void validateDuplicateEmail(String email);

    void validateDuplicateNickname(String nickname);

    void createMember(Member member);

    List<UserRankingDto> findAllOrderByCash(Long limit , Long offset);

    List<UserRankingDto> findAllOrderByTotalScore(Long limit , Long offset);

}
