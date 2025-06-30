package io.cavia.trader.module.game.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserDto {

    private long userId;
    private BigDecimal returnRate;
    private int gameRank;
    private int earnedScore;
    private int postScore;
    private long earnedCash;
    private long postCash;
}
