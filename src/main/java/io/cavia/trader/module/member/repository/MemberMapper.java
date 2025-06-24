package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    /**
     * 회원 정보를 DB에 저장합니다.
     *
     * @param member 저장할 회원 객체
     */
    void save(Member member);

    /**
     * 이메일로 회원을 조회합니다. (로그인 등에서 사용)
     *
     * @param email 조회할 이메일
     * @return 조회된 회원 객체 (없을 경우 Optional.empty())
     */
    Optional<Member> findByEmail(String email);

    /**
     * 이메일이 이미 있는지 조회합니다.
     *
     * @param email 조회할 이메일
     * @return 이메일이 있으면 true, 없으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 닉네임이 이미 있는지 조회합니다.
     *
     * @param nickname 조회할 닉네임
     * @return 닉네임이 있으면 true, 없으면 false
     */
    boolean existsByNickname(String nickname);

}