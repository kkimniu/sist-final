package io.cavia.trader.module.auth.dto;

import io.cavia.trader.common.validation.ValidEmail;
import lombok.Data;

@Data
public class SendCodeRequestDto {

    @ValidEmail
    private String email;
}
