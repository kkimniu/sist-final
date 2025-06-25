package io.cavia.trader.module.member.mypage.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NicknameUpdateRequestDto {
    private int id;
    private String nickname;
}
