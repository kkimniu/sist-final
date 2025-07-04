package io.cavia.trader.module.auth.dto;

import io.cavia.trader.common.validation.ValidAuthKey;
import io.cavia.trader.common.validation.ValidEmail;
import lombok.Data;

@Data
public class VerifyCodeRequestDto {

    @ValidEmail
    String email;

    @ValidAuthKey
    String authKey;
}
