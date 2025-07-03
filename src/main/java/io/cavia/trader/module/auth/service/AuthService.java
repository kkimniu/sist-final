package io.cavia.trader.module.auth.service;

import io.cavia.trader.module.auth.dto.LoginRequestDto;
import io.cavia.trader.module.auth.dto.ResetPasswordRequestDto;
import io.cavia.trader.module.auth.dto.SignupDto;
import io.cavia.trader.module.member.entity.Member;

public interface AuthService {
    void sendVerificationEmail(String email);

    void verifyAuthKey(String email, String authKey);

    void validateDuplicateEmail(String email);

    void validateDuplicateNickname(String nickname);

    void join(SignupDto signupDto);

    String login(LoginRequestDto requestDto);

    void sendAuthEmail(String to, String authKey);

    boolean isMemberByEmail(String email);

    void resetPassword(ResetPasswordRequestDto requestDto);
}
