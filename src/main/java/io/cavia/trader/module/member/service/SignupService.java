package io.cavia.trader.module.member.service;

public interface SignupService {
    void sendVerificationEmail(String email);

    void validateDuplicateEmail(String email);

    void validateDuplicateNickname(String nickname);
}
