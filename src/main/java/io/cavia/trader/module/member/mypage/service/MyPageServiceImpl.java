package io.cavia.trader.module.member.mypage.service;

import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.mypage.dto.GameParticipationDto;
import io.cavia.trader.module.member.mypage.mapper.MyPageMapper;
import io.cavia.trader.module.member.repository.MemberMapper;
import io.cavia.trader.module.notice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final MyPageMapper mypageMapper;
    private final MemberMapper memberMapper;

    @Override
    public List<GameParticipationDto> findByMemberId(int memberId) {
        List<GameParticipationDto> list = mypageMapper.findByMemberId(memberId);
        if (list == null || list.size() == 0) {
            throw new NotFoundException("사용자가 존재하지 않습니다");
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

}
