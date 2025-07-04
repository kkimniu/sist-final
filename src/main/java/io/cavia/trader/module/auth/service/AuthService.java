package io.cavia.trader.module.auth.service;

import io.cavia.trader.module.auth.dto.LoginRequestDto;
import io.cavia.trader.module.auth.dto.ResetPasswordRequestDto;
import io.cavia.trader.module.auth.dto.SignupDto;

public interface AuthService {
    void sendVerificationEmail(String email);

    void verifyPasswordResetVerificationRequest(String email, String authKey);

    void verifySignupVerificationRequest(String email, String authKey);

    void validateDuplicateEmail(String email);

    void validateDuplicateNickname(String nickname);

    void join(SignupDto signupDto);

    String login(String email, String password);

    void resetPassword(ResetPasswordRequestDto requestDto);
}
