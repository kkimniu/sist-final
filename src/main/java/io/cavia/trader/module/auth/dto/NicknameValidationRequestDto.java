package io.cavia.trader.module.auth.dto;

import io.cavia.trader.common.validation.ValidNickname;
import lombok.Data;

@Data
public class NicknameValidationRequestDto {

    @ValidNickname
    String nickname;
}
