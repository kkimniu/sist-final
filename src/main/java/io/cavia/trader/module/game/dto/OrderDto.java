package io.cavia.trader.module.game.dto;

import lombok.*;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderDto {
    @Builder.Default
    private Queue<OrderTableDto> orderTableDto = new LinkedList<>();
    @Builder.Default
    private Queue<TradeLog> tradeLogs = new LinkedList<>();
    private int QuantityOfMarketBuy;
    private int QuantityOfMarketSell;
}
