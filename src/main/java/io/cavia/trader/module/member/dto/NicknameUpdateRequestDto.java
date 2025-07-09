package io.cavia.trader.module.member.dto;

import io.cavia.trader.common.validation.ValidNickname;
import lombok.Getter;

@Getter
public class NicknameUpdateRequestDto {

    @ValidNickname
    private String nickname;
}
