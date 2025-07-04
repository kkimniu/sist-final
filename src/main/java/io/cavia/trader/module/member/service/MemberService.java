package io.cavia.trader.module.member.service;

import io.cavia.trader.module.member.dto.UserRankingDto;
import io.cavia.trader.module.member.entity.GameParticipation;
import io.cavia.trader.module.member.entity.Member;

import java.util.List;

public interface MemberService {

    List<GameParticipation> getGameParticipationByMemberId(Long memberId);

    Member getMemberById(Long id);

    Member getMemberByEmail(String email);

    void changeNickname(Long id, String nickname);

    void validatePassword(Long id, String password);

    void processPasswordChangeRequest(Long id, String currentPassword, String newPassword);

    void changePassword(Long id, String newPassword);

    void resetCash(Long id);

    void withdrawMember(Long id, String password);

    void validateDuplicateEmail(String email);

    void validateDuplicateNickname(String nickname);

    void createMember(Member member);

    List<UserRankingDto> findAllOrderByCash(Long limit, Long offset);

    List<UserRankingDto> findAllOrderByTotalScore(Long limit, Long offset);

}
