package io.cavia.trader.module.auth.dto;

import io.cavia.trader.common.validation.ValidEmail;
import io.cavia.trader.common.validation.ValidPassword;
import lombok.Data;

@Data
public class LoginRequestDto {

    @ValidEmail
    private String username;

    @ValidPassword
    private String password;
}
