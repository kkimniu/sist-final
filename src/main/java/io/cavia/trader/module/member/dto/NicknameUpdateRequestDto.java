package io.cavia.trader.module.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class NicknameUpdateRequestDto {
    
    @NotEmpty(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,16}$", message = "닉네임은 1자 이상 16자 이하로 입력해주세요.")
    private String nickname;
}
