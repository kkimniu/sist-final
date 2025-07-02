package io.cavia.trader.module.auth.dto;

import io.cavia.trader.common.validation.ValidAuthKey;
import io.cavia.trader.common.validation.ValidEmail;
import io.cavia.trader.common.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ResetPasswordRequestDto {

    @ValidEmail
    private String email;

    @ValidAuthKey
    private String authKey;

    @ValidPassword
    private String password;
}
