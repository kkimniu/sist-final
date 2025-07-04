package io.cavia.trader.module.auth.service;

import io.cavia.trader.module.auth.dto.SignupDto;
import io.cavia.trader.module.member.entity.Member;

public interface AuthService {
    void sendVerificationEmail(String email);

    void verifyPasswordResetVerificationRequest(String email, String authKey);

    void verifySignupVerificationRequest(String email, String authKey);

    void validateDuplicateNickname(String nickname);

    Member join(SignupDto signupDto);

    String login(String email, String password);

    void resetPassword(String email, String authKey, String rawPassword);
}
