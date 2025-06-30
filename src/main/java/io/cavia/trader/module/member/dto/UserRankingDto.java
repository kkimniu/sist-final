package io.cavia.trader.module.member.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRankingDto {
    private String nickname;
    private Long cash;
}
