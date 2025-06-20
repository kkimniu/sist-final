package io.cavia.trader.module.member.service;

import io.cavia.trader.common.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final EmailService emailService;

    @Override
    public void sendVerificationEmail(String email) {
        String authKey = String.valueOf((int)(Math.random() * 900000 + 100000));
        emailService.sendAuthEmail(email, authKey);
    }

    @Override
    public void validateDuplicateEmail(String email) {

    }

    @Override
    public void validateDuplicateNickname(String nickname) {

    }
}
