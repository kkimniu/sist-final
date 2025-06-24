package io.cavia.trader.module.member.repository;

import io.cavia.trader.module.member.entity.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MybatisEmailVerificationRepository implements EmailVerificationRepository {

    private final EmailVerificationMapper emailVerificationMapper;


    @Override
    public void save(EmailVerification emailVerification) {
        emailVerificationMapper.saveOrUpdate(emailVerification);
    }

    @Override
    public Optional<EmailVerification> findByEmail(String email) {
        return emailVerificationMapper.findByEmail(email);
    }
}
