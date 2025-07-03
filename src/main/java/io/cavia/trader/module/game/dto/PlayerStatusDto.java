package io.cavia.trader.module.game.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PlayerStatusDto {
    private long memberId;
    @Builder.Default
    private boolean Updated = false;
    private String memberNickname;
    private Long gameId;
    private BigDecimal returnRate;
    private int stocksHolding;
    private int gameRank;
    private int earnedScore;
    private int postScore;
    private long earnedCash;
    private long postCash;
    private OrderDto orderDto;
    private LocalDateTime enteredAt;
}
