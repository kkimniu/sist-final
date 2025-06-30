package io.cavia.trader.module.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ResetPasswordRequestDto {

    /**
     * 이메일 필드
     */
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 254, message = "이메일은 254자 이하로 입력해주세요.")
    private String email;

    /**
     * 이메일 인증키 필드
     */
    @NotEmpty(message = "인증키를 입력해주세요.")
    @Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리 숫자입니다.")
    private String authKey;

    /**
     * 비밀번호 필드
     */
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하로 입력해주세요")
    private String password;
}
