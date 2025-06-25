package io.cavia.trader.module.member.mypage.service;

import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.mypage.dto.GameParticipationDto;
import io.cavia.trader.module.member.mypage.mapper.MyPageMapper;
import io.cavia.trader.module.member.repository.MemberMapper;
import io.cavia.trader.module.notice.exception.InvalidNoticeRequestException;
import io.cavia.trader.module.notice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public void changeNickname(Long id, String nickname, LocalDateTime nicknameUpdatedAt) {
        int result = memberMapper.updateNickname(id,nickname,nicknameUpdatedAt);
        if(result<=0){
            throw new InvalidNoticeRequestException("닉네임 변경 실패");
        }
    }

    @Override
    public boolean validateDuplicatePassword(int id, String password) {
        return memberMapper.existsByIdAndPassword(id,password);
    }

    @Override
    public int changePassword(int id, String password, LocalDateTime passwordUpdatedAt) {
        return memberMapper.updatePassword(id,password,passwordUpdatedAt);
    }

}
