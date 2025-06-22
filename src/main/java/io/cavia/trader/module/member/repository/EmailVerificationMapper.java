package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.entity.EmailVerification;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface EmailVerificationMapper {

    /**
     * 이메일 인증 정보를 저장하거나, 이미 존재하면 최신 정보로 업데이트합니다.
     * (INSERT ... ON DUPLICATE KEY UPDATE 구문에 해당)
     * @param emailVerification 저장 또는 업데이트할 인증 정보 객체
     */
    void saveOrUpdate(EmailVerification emailVerification);

    /**
     * 이메일 주소로 최신 인증 정보를 조회합니다.
     * @param email 조회할 이메일 주소
     * @return 조회된 인증 정보 객체 (없을 경우 Optional.empty())
     */
    Optional<EmailVerification> findByEmail(String email);
}
