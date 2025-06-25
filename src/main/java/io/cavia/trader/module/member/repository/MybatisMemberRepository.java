package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MybatisMemberRepository implements MemberRepository {

    private final MemberMapper memberMapper;

    @Override
    public void save(Member member) {
        memberMapper.save(member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }

    @Override
    public int updateNickname(int id, String nickname, LocalDateTime nicknameUpdatedAt) {return memberMapper.updateNickname(id,nickname,nicknameUpdatedAt);}

    @Override
    public int updatePassword(int id, String password, LocalDateTime passwordUpdatedAt) {return memberMapper.updatePassword(id,password,passwordUpdatedAt);}

    @Override
    public int updateCash(int id, Long cash) {return memberMapper.updateCash(id,cash);}

    @Override
    public boolean existsByEmail(String email) {
        return memberMapper.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberMapper.existsByNickname(nickname);
    }

    @Override
    public boolean existsByIdAndPassword(int id,String password) {return memberMapper.existsByIdAndPassword(id,password);}
}
