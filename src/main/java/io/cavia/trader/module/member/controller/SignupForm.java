package io.cavia.trader.module.member.controller;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    // --- Fields ---

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

}