package io.cavia.trader.module.member.mypage.service;

import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.mypage.dto.GameParticipationDto;
import io.cavia.trader.module.member.mypage.mapper.MyPageMapper;
import io.cavia.trader.module.member.repository.MemberMapper;
import io.cavia.trader.module.member.repository.MemberRepository;
import io.cavia.trader.module.notice.exception.InvalidNoticeRequestException;
import io.cavia.trader.module.notice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final MyPageMapper mypageMapper;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public List<GameParticipationDto> findByMemberId(int memberId) {
        List<GameParticipationDto> list = mypageMapper.findByMemberId(memberId);
        if (list == null || list.size() == 0) {
            throw new NotFoundException("게임 참여 이력이 없습니다");
        }
        return list;
    }

    @Override
    public Member findById(Long id) {
        Optional<Member> optional = memberMapper.findById(id);
        Member member;
        if (optional.isPresent()) {
            member = optional.get();
        } else {
            throw new NotFoundException("사용자가 존재하지 않습니다");
        }
        return member;
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
    public boolean validatePassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        return passwordEncoder.matches(password, member.getPassword());
    }

    @Override
    public int changePassword(Long id, String password, LocalDateTime passwordUpdatedAt) {
        return memberRepository.updatePassword(id, password, passwordUpdatedAt);
    }

    @Override
    public int resetCash(int id) {
        return memberRepository.updateCash(id, 90000000L);
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

}
