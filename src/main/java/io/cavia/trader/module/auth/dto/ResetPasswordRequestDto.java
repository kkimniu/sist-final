package io.cavia.trader.module.auth.dto;

import io.cavia.trader.common.validation.ValidAuthKey;
import io.cavia.trader.common.validation.ValidEmail;
import io.cavia.trader.common.validation.ValidPassword;
import lombok.Data;

@Data
public class ResetPasswordRequestDto {

    @ValidEmail
    private String email;

    @ValidAuthKey
    private String authKey;

    @ValidPassword
    private String password;
}
