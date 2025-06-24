package io.cavia.trader.module.member.service;

import io.cavia.trader.module.member.dto.LoginRequestDto;
import io.cavia.trader.module.member.dto.SignupForm;

public interface MemberService {
    void sendVerificationEmail(String email);

    void verifyAuthKey(String email, String authKey);

    void validateDuplicateEmail(String email);

    void validateDuplicateNickname(String nickname);

    void join(SignupForm signupForm);

    String login(LoginRequestDto requestDto);
}
