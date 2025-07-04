package io.cavia.trader.module.game.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TradeLog {

    private String Id;
    private int price;
    private int quantity;
    private LocalDateTime createdAt;
}
