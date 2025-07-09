package io.cavia.trader.module.member.dto;

import io.cavia.trader.common.validation.ValidPassword;
import lombok.Getter;

@Getter
public class PasswordVerificationRequestDto {

    @ValidPassword
    String currentPassword;
}
