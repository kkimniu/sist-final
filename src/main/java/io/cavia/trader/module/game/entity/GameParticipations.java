package io.cavia.trader.module.game.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GameParticipations {
    private long memberId;
    private Long gameId;
    private BigDecimal returnRate;
    private int gameRank;
    private int earnedScore;
    private int postScore;
    private long earnedCash;
    private long postCash;
    private LocalDateTime enteredAt;
}
