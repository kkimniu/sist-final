package io.cavia.trader.module.auth.service;

import io.cavia.trader.common.email.EmailService;
import io.cavia.trader.module.auth.dto.LoginRequestDto;
import io.cavia.trader.module.auth.dto.ResetPasswordRequestDto;
import io.cavia.trader.module.auth.dto.SignupForm;
import io.cavia.trader.module.auth.entity.EmailVerification;
import io.cavia.trader.module.auth.repository.EmailVerificationRepository;
import io.cavia.trader.module.jwt.JwtUtil;
import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
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
        memberService.validateDuplicateEmail(email);
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        memberService.validateDuplicateNickname(nickname);
    }

    public void validateTermsAgreement() {

    }

    @Override
    public void join(SignupForm signupForm) {

        Member member = Member.builder()
                .email(signupForm.getEmail())
                .nickname(signupForm.getNickname())
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .cash(memberCashDefault)
                .build();

        memberService.validateDuplicateNickname(signupForm.getNickname());
        memberService.validateDuplicateEmail(signupForm.getEmail());
        verifyAuthKey(signupForm.getEmail(), signupForm.getAuthKey());
        memberService.createMember(member);
        System.out.println("회원가입 완료시 member = " + member);
    }

    /**
     * 로그인 비즈니스 로G직
     *
     * @param requestDto 로그인 요청 정보
     * @return 생성된 JWT
     */
    @Override
    public String login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 1. 사용자 확인
        // .orElseThrow() : Optional 객체가 비어있을 경우 예외를 던짐
        Member member = memberService.getMemberByEmail(username);

        // 2. 비밀번호 확인
        // passwordEncoder.matches(평문, 암호화된 비밀번호)
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 생성 및 반환
        return jwtUtil.createToken(member.getId(), member.getRole());
    }

    @Override
    public Member getMemberById(Long id) {
        return memberService.getMemberById(id);
    }

    /**
     * 사용자의 이메일 인증을 위해 인증키가 포함된 이메일을 발송합니다.
     *
     * @param to      메일 받을 주소
     * @param authKey 사용자에게 전달할 이메일 인증을 위한 인증키
     */
    @Override
    public void sendAuthEmail(String to, String authKey) {
        Context context = new Context();
        context.setVariable("username", to);
        context.setVariable("authKey", authKey);
        // 템플릿을 사용하여 HTML 본문 생성
        String htmlBody = templateEngine.process("email/auth-email", context);

        emailService.sendEmail(to, "[TRADER.IO] 이메일 인증", htmlBody);
    }

    @Override
    public boolean isMemberByEmail(String email) {
        return memberService.isMemberByEmail(email);
    }

    @Override
    public void resetPassword(ResetPasswordRequestDto requestDto) {
        verifyAuthKey(requestDto.getEmail(), requestDto.getAuthKey());

        Member member = memberService.getMemberByEmail(requestDto.getEmail());

        String newEncodedPassword = passwordEncoder.encode(requestDto.getPassword());
        if (memberRepository.updatePassword(member.getId(), newEncodedPassword, LocalDateTime.now()) == 0) {
            throw new IllegalStateException("비밀번호 수정 작업이 실패했습니다.");
        }
    }
}
