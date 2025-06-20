package io.cavia.trader.module.member.controller;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class SignupForm {

    // --- Validation Groups ---
    // 한 파일 내에서 관리할 수 있도록 static inner class로 포함시켰습니다.
    public static class ValidationGroups {
        public interface TermsGroup {
        }

        public interface EmailGroup {
        }

        public interface AuthKeyGroup {
        }

        public interface NicknameGroup {
        }

        public interface PasswordGroup {
        }
    }

    /**
     * 약관 동의 필드
     * 이 필드들이 true가 되어야만 유효성 검사를 통과합니다.
     */
    @AssertTrue(message = "서비스 이용약관에 동의해야 합니다.", groups = ValidationGroups.TermsGroup.class)
    private boolean termsOfServiceAgreed;
    @AssertTrue(message = "개인정보 처리방침에 동의해야 합니다.", groups = ValidationGroups.TermsGroup.class)
    private boolean privacyPolicyAgreed;

    /**
     * 이메일 필드
     */
    @NotEmpty(message = "이메일을 입력해주세요.", groups = ValidationGroups.EmailGroup.class)
    @Email(message = "올바른 이메일 형식이 아닙니다.", groups = ValidationGroups.EmailGroup.class)
    @Size(max = 254, message = "이메일은 254자 이하로 입력해주세요.", groups = ValidationGroups.EmailGroup.class)
    private String email;

    /**
     * 이메일 인증키 필드
     */
    @NotEmpty(message = "인증키를 입력해주세요.", groups = ValidationGroups.AuthKeyGroup.class)
    @Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리 숫자입니다.", groups = ValidationGroups.AuthKeyGroup.class)
    private String authKey;

    /**
     * 닉네임 필드
     */
    @NotEmpty(message = "닉네임을 입력해주세요.", groups = ValidationGroups.NicknameGroup.class)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,16}$", message = "닉네임은 1자 이상 16자 이하로 입력해주세요.", groups = ValidationGroups.NicknameGroup.class)
    private String nickname;

    /**
     * 비밀번호 필드
     */
    @NotEmpty(message = "비밀번호를 입력해주세요.", groups = ValidationGroups.PasswordGroup.class)
    @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하로 입력해주세요", groups = ValidationGroups.PasswordGroup.class)
    private String password;

    /**
     * 비밀번호 확인 필드
     */
    @NotEmpty(message = "비밀번호를 다시 한번 입력해주세요.", groups = ValidationGroups.PasswordGroup.class)
    private String passwordConfirm;
}
