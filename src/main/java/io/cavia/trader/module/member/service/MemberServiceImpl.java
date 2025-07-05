package io.cavia.trader.module.member.service;

import io.cavia.trader.common.exception.ApiException;
import io.cavia.trader.common.exception.ErrorCode;
import io.cavia.trader.module.member.dto.UserRankingDto;
import io.cavia.trader.module.member.entity.GameParticipation;
import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.repository.GameParticipationRepository;
import io.cavia.trader.module.member.repository.MemberMapper;
import io.cavia.trader.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final GameParticipationRepository gameParticipationRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Value("${score.rank_max_score:5000}")
    private Integer maxTotalScore;

    @Value("${member.cash.default:10000000L}")
    private Long memberCashDefault;

    @Value("${member.cash.reset:5000000L}")
    private Long memberCashReset;

    @Override
    public List<GameParticipation> getGameParticipationByMemberId(Long memberId) {
        List<GameParticipation> list = gameParticipationRepository.findByMemberId(memberId);
        if (list == null || list.isEmpty()) {
            throw new ApiException(ErrorCode.GAME_HISTORY_NOT_FOUND);
        }
        return list;
    }

    @Override
    public List<GameParticipation> getGameParticipationByMemberIdWithPaging(Long memberId, int limit, int offset) {
        List<GameParticipation> list = gameParticipationRepository.findByMemberIdWithPaging(memberId, limit, offset);
        if (list == null || list.isEmpty()) {
            throw new ApiException(ErrorCode.GAME_HISTORY_NOT_FOUND);
        }
        return list;
    }

    @Override
    public Long getGameParticipationCountByMemberId(Long memberId) {
        return gameParticipationRepository.countByMemberId(memberId);
    }

    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public void changeNickname(Long id, String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new ApiException(ErrorCode.DUPLICATE_NICKNAME);
        }
        if (memberRepository.updateNickname(id, nickname, LocalDateTime.now()) <= 0) {
            throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @Override
    public void validatePassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorCode.MEMBER_NOT_FOUND)
        );
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new ApiException(ErrorCode.INCORRECT_PASSWORD);
        }
    }

    @Override
    public void processPasswordChangeRequest(Long id, String currentPassword, String newPassword) {
        validatePassword(id, currentPassword);
        changePassword(id, newPassword);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        String newEncodedPassword = passwordEncoder.encode(newPassword);
        if (memberRepository.updatePassword(id, newEncodedPassword, LocalDateTime.now()) == 0) {
            throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @Override
    public void resetCash(Long id) {
        if (memberRepository.updateCash(id, memberCashReset) == 0) {
            throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @Override
    public void withdrawMember(Long id, String password) {
        validatePassword(id, password);
        if (memberMapper.delete(id) == 0) {
            throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @Override
    public void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new ApiException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new ApiException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    @Override
    public void createMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member createMember(String email, String password, String nickname) {
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .totalScore(maxTotalScore / 2)
                .cash(memberCashDefault)
                .build();

        memberRepository.save(member);
        return member;
    }

    @Override
    public List<UserRankingDto> findAllOrderByCash(Long limit, Long offset) {
        List<UserRankingDto> list = memberRepository.findAllByOrderByCashDesc(limit, offset);
        if (list == null || list.isEmpty()) {
            throw new ApiException(ErrorCode.USER_RANKING_NOT_FOUND);
        }
        return list;
    }

    @Override
    public List<UserRankingDto> findAllOrderByTotalScore(Long limit, Long offset) {
        List<UserRankingDto> list = memberRepository.findAllByOrderByTotalScoreDesc(limit, offset);
        if (list == null || list.isEmpty()) {
            throw new ApiException(ErrorCode.USER_RANKING_NOT_FOUND);
        }
        return list;
    }

}
