package io.cavia.trader.module.member.service;

import io.cavia.trader.module.member.controller.SignupForm;

public interface SignupService {
    void sendVerificationEmail(String email);

    void verifyAuthKey(String email, String authKey);

    void validateDuplicateEmail(String email);

    void validateDuplicateNickname(String nickname);

    void join(SignupForm signupForm);
}
