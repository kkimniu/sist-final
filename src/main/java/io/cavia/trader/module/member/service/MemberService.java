package io.cavia.trader.module.member.service;

import io.cavia.trader.module.member.dto.LoginRequestDto;
import io.cavia.trader.module.member.dto.ResetPasswordRequestDto;
import io.cavia.trader.module.member.dto.SignupForm;
import io.cavia.trader.module.member.entity.Member;

import java.util.Optional;

public interface MemberService {
    void sendVerificationEmail(String email);

    void verifyAuthKey(String email, String authKey);

    void validateDuplicateEmail(String email);

    void validateDuplicateNickname(String nickname);

    void join(SignupForm signupForm);

    String login(LoginRequestDto requestDto);

    Member getMemberById(Long email);

    void sendAuthEmail(String to, String authKey);

    boolean isOurMember(String email);

    void resetPassword(ResetPasswordRequestDto requestDto);
}
