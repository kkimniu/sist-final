package io.cavia.trader.module.auth.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {

    private String email;

    private String verificationKey;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    /**
     * 새로운 이메일 인증 객체를 생성하는 정적 팩토리 메서드입니다.
     * 생성 로직을 캡슐화하여 서비스 코드의 가독성과 일관성을 높여줍니다.
     *
     * @param email             인증을 받을 이메일 주소
     * @param verificationKey   생성된 인증키
     * @param validityInMinutes 유효 기간(분)
     * @return 초기화된 EmailVerification 객체
     */
    public static EmailVerification create(String email, String verificationKey, int validityInMinutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(validityInMinutes);

        EmailVerification verification = new EmailVerification();
        verification.setEmail(email);
        verification.setVerificationKey(verificationKey);
        verification.setExpiresAt(expiresAt);
        verification.setCreatedAt(now);
        return verification;
    }
}