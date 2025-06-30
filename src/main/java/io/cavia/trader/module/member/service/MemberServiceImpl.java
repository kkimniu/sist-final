package io.cavia.trader.module.member.service;

import io.cavia.trader.module.member.dto.GameParticipationDto;
import io.cavia.trader.module.member.dto.PasswordChangeRequestDto;
import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.repository.GameParticipationRepository;
import io.cavia.trader.module.member.repository.MemberMapper;
import io.cavia.trader.module.member.repository.MemberRepository;
import io.cavia.trader.module.notice.exception.InvalidNoticeRequestException;
import io.cavia.trader.module.notice.exception.NotFoundException;
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

    @Value("${member.cash.reset}")
    private Long memberCashReset;

    @Override
    public List<GameParticipationDto> getGameParticipationByMemberId(int memberId) {
        List<GameParticipationDto> list = gameParticipationRepository.findByMemberId(memberId);
        if (list == null || list.size() == 0) {
            throw new NotFoundException("게임 참여 이력이 없습니다");
        }
        return list;
    }

    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("회원번호로 조회된 회원이 없습니다."));
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberMapper.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("이메일로 조회된 회원이 없습니다."));
    }

    @Override
    public void changeNickname(Long id, String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
        if (memberRepository.updateNickname(id, nickname, LocalDateTime.now()) <= 0) {
            throw new InvalidNoticeRequestException("닉네임 변경 실패");
        }
    }

    @Override
    public void validatePassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public void changePassword(Long id, PasswordChangeRequestDto requestDto) {
        validatePassword(id, requestDto.getCurrentPassword());
        if (memberRepository.updatePassword(id, requestDto.getNewPassword(), LocalDateTime.now()) == 0) {
            throw new IllegalStateException("비밀번호 수정 작업이 실패했습니다.");
        }
    }

    @Override
    public void resetCash(Long id) {
        if (memberRepository.updateCash(id, memberCashReset) == 0) {
            throw new IllegalStateException("자산 초기화가 실패했습니다.");
        }
    }

    @Override
    public int deleteMember(Long id, String password) {

        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return memberMapper.delete(id);
    }

    @Override
    public boolean isMemberByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }

    @Override
    public void createMember(Member member) {
        memberRepository.save(member);
    }

}
