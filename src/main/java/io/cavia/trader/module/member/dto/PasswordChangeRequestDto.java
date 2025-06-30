package io.cavia.trader.module.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordChangeRequestDto {

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    String currentPassword;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하로 입력해주세요")
    String newPassword;
}
