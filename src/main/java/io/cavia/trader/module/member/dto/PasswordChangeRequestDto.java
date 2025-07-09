package io.cavia.trader.module.member.dto;

import io.cavia.trader.common.validation.ValidPassword;
import lombok.Getter;

@Getter
public class PasswordChangeRequestDto {

    @ValidPassword
    String currentPassword;

    @ValidPassword
    String newPassword;
}
