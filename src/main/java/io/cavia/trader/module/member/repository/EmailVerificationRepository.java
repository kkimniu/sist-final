package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.entity.EmailVerification;

import java.util.Optional;

public interface EmailVerificationRepository {

    /**
     * 이메일 인증 객체를 저장합니다.
     * 이미 같은 이메일의 인증 객체가 존재할 경우, 최신 정보로 덮어씁니다.
     *
     * @param emailVerification 저장 또는 업데이트할 인증 정보 객체
     */
    void save(EmailVerification emailVerification);

    /**
     * 이메일 주소로 최신 인증 정보를 조회합니다.
     *
     * @param email 조회할 이메일 주소
     * @return 조회된 인증 정보 객체 (없을 경우 Optional.empty())
     */
    Optional<EmailVerification> findByEmail(String email);
}
