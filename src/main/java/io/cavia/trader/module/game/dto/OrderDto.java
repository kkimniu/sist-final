package io.cavia.trader.module.game.dto;

import lombok.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderDto {
    @Builder.Default
    private Queue<OrderTableDto> orderTableDtos = new ConcurrentLinkedQueue<>();
    @Builder.Default
    private Queue<TradeLog> tradeLogs = new ConcurrentLinkedQueue<>();
    private int QuantityOfMarketBuy;
    private int QuantityOfMarketSell;
}
