package io.cavia.trader.module.auth.service;

import io.cavia.trader.common.email.EmailService;
import io.cavia.trader.common.exception.ApiException;
import io.cavia.trader.common.exception.ErrorCode;
import io.cavia.trader.module.auth.dto.SignupDto;
import io.cavia.trader.module.auth.entity.EmailVerification;
import io.cavia.trader.module.auth.repository.EmailVerificationRepository;
import io.cavia.trader.module.jwt.JwtUtil;
import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TemplateEngine templateEngine;
    private final MemberService memberService;

    @Value("${member.cash.default}")
    private Long memberCashDefault;

    @Override
    public void sendVerificationEmail(String email) {
        String authKey = String.valueOf((int) (Math.random() * 900000 + 100000));

        sendAuthEmail(email, authKey);
        emailVerificationRepository.save(EmailVerification.create(email, authKey, 60));
    }

    @Override
    public void verifyPasswordResetVerificationRequest(String email, String authKey) {
        verifyAuthKey(email, authKey);
        memberService.getMemberByEmail(email);
    }

    @Override
    public void verifySignupVerificationRequest(String email, String authKey) {
        verifyAuthKey(email, authKey);
        memberService.validateDuplicateEmail(email);
    }

    private void verifyAuthKey(String email, String authKey) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_VERIFICATION_NOT_FOUND));
        if (!emailVerification.getVerificationKey().equals(authKey)) {
            throw new ApiException(ErrorCode.INVALID_AUTH_KEY);
        }
        if (emailVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ApiException(ErrorCode.EXPIRED_AUTH_KEY);
        }
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        memberService.validateDuplicateNickname(nickname);
    }

    @Override
    public Member join(SignupDto signupDto) {

        Member member = Member.builder()
                .email(signupDto.getEmail())
                .nickname(signupDto.getNickname())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .cash(memberCashDefault)
                .build();

        memberService.validateDuplicateNickname(signupDto.getNickname());
        memberService.validateDuplicateEmail(signupDto.getEmail());
        verifyAuthKey(signupDto.getEmail(), signupDto.getAuthKey());
        memberService.createMember(member);
        System.out.println("회원가입 완료 member = " + member);
        return member;
    }

    /**
     * 로그인 비즈니스 로직
     *
     * @param email    로그인 시도 이메일
     * @param password 로그인 시도 비밀번호
     * @return 생성된 JWT
     */
    @Override
    public String login(String email, String password) {
        try {
            Member member = memberService.getMemberByEmail(email);
            memberService.validatePassword(member.getId(), password);
            return jwtUtil.createToken(member.getId(), member.getRole());
        } catch (ApiException e) {
            throw new ApiException(ErrorCode.LOGIN_FAILED);
        }
    }

    /**
     * 사용자의 이메일 인증을 위해 인증키가 포함된 이메일을 발송합니다.
     *
     * @param to      메일 받을 주소
     * @param authKey 사용자에게 전달할 이메일 인증을 위한 인증키
     */
    private void sendAuthEmail(String to, String authKey) {
        Context context = new Context();
        context.setVariable("username", to);
        context.setVariable("authKey", authKey);
        // 템플릿을 사용하여 HTML 본문 생성
        String htmlBody = templateEngine.process("email/auth-email", context);

        emailService.sendEmail(to, "[TRADER.IO] 이메일 인증", htmlBody);
    }

    @Override
    public void resetPassword(String email, String authKey, String rawPassword) {
        verifyAuthKey(email, authKey);
        Member member = memberService.getMemberByEmail(email);
        memberService.changePassword(member.getId(), rawPassword);
    }
}
