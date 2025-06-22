package io.cavia.trader.module.member.service;

import io.cavia.trader.common.email.EmailService;
import io.cavia.trader.module.member.entity.EmailVerification;
import io.cavia.trader.module.member.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    public void sendVerificationEmail(String email) {
        String authKey = String.valueOf((int) (Math.random() * 900000 + 100000));
        try {
            emailService.sendAuthEmail(email, authKey);
            emailVerificationRepository.save(EmailVerification.create(email, authKey, 60));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void validateDuplicateEmail(String email) {

    }

    @Override
    public void validateDuplicateNickname(String nickname) {

    }
}
