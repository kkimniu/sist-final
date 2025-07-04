package io.cavia.trader.module.member.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GameParticipation {
    private Long id;
    private long memberId;
    private long gameId;
    private BigDecimal returnRate;
    private int gameRank;
    private int earnedScore;
    private int postScore;
    private Long earnedCash;
    private Long postCash;
    private LocalDateTime enteredAt;
}
