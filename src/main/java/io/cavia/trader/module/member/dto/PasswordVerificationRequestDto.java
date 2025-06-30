package io.cavia.trader.module.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class PasswordVerificationRequestDto {

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    String currentPassword;
}
