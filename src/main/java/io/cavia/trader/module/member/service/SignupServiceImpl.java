package io.cavia.trader.module.member.service;

import io.cavia.trader.common.email.EmailService;
import io.cavia.trader.module.member.controller.SignupForm;
import io.cavia.trader.module.member.entity.EmailVerification;
import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.repository.EmailVerificationRepository;
import io.cavia.trader.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MemberRepository memberRepository;

    @Value("${member.default.cash}")
    private Long memberDefaultCash;

    @Override
    public void sendVerificationEmail(String email) {
        String authKey = String.valueOf((int) (Math.random() * 900000 + 100000));

        emailService.sendAuthEmail(email, authKey);
        emailVerificationRepository.save(EmailVerification.create(email, authKey, 60));
    }

    public void verifyAuthKey(String email, String authKey) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 조회된 인증키 정보가 없습니다."));
        if (!emailVerification.getVerificationKey().equals(authKey)) {
            throw new IllegalArgumentException("인증키가 일치하지 않습니다.");
        }
        if (emailVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("만료된 인증키 입니다.");
        }
    }

    @Override
    public void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }

    public void validateTermsAgreement() {

    }

    @Override
    public void join(SignupForm signupForm) {

        Member member = Member.builder()
                .email(signupForm.getEmail())
                .nickname(signupForm.getNickname())
                .password(signupForm.getPassword())
                .cash(memberDefaultCash)
                .build();

        validateDuplicateNickname(signupForm.getNickname());
        validateDuplicateEmail(signupForm.getEmail());
        verifyAuthKey(signupForm.getEmail(), signupForm.getAuthKey());
        memberRepository.save(member);
        System.out.println("회원가입 완료시 member = " + member);
    }
}
