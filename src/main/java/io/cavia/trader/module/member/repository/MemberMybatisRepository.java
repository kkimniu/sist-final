package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.dto.UserRankingDto;
import io.cavia.trader.module.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberMybatisRepository implements MemberRepository {

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
    public int updateNickname(Long id, String nickname, LocalDateTime nicknameUpdatedAt) {
        return memberMapper.updateNickname(id, nickname, nicknameUpdatedAt);
    }

    @Override
    public int updatePassword(Long id, String password, LocalDateTime passwordUpdatedAt) {
        return memberMapper.updatePassword(id, password, passwordUpdatedAt);
    }

    @Override
    public int updateCash(Long id, Long cash) {
        return memberMapper.updateCash(id, cash);
    }

    @Override
    public int delete(Long id) {
        return memberMapper.delete(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberMapper.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberMapper.existsByNickname(nickname);
    }

    @Override
    public boolean existsByIdAndPassword(Long id, String password) {
        return memberMapper.existsByIdAndPassword(id, password);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberMapper.findById(id);
    }

    @Override
    public List<UserRankingDto> findAllOrderByCash(int limit , int offset) {
        return memberMapper.findAllOrderByCash(limit,offset);
    }

    @Override
    public List<UserRankingDto> findAllOrderByTotalScore(int limit , int offset) {
        return memberMapper.findAllOrderByTotalScore(limit,offset);
    }
}
