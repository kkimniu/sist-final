package io.cavia.trader.module.member.service;

import io.cavia.trader.common.email.EmailService;
import io.cavia.trader.module.jwt.JwtUtil;
import io.cavia.trader.module.member.dto.LoginRequestDto;
import io.cavia.trader.module.member.dto.SignupForm;
import io.cavia.trader.module.member.entity.EmailVerification;
import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.repository.EmailVerificationRepository;
import io.cavia.trader.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .cash(memberDefaultCash)
                .build();

        validateDuplicateNickname(signupForm.getNickname());
        validateDuplicateEmail(signupForm.getEmail());
        verifyAuthKey(signupForm.getEmail(), signupForm.getAuthKey());
        memberRepository.save(member);
        System.out.println("회원가입 완료시 member = " + member);
    }

    /**
     * 로그인 비즈니스 로G직
     *
     * @param requestDto 로그인 요청 정보
     * @return 생성된 JWT
     */
    public String login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 1. 사용자 확인
        // .orElseThrow() : Optional 객체가 비어있을 경우 예외를 던짐
        Member member = memberRepository.findByEmail(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 2. 비밀번호 확인
        // passwordEncoder.matches(평문, 암호화된 비밀번호)
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 생성 및 반환
        return jwtUtil.createToken(member.getId(), member.getRole());
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("조회된 사용자가 없습니다."));
    }
}
