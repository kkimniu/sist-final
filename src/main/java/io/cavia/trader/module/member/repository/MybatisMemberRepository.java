package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public boolean existsByEmail(String email) {
        return memberMapper.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberMapper.existsByNickname(nickname);
    }
}
