package io.cavia.trader.module.game.dto;

import lombok.*;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {
    @Builder.Default
    private Map<Price, Integer> buyDeals = new HashMap<>();
    @Builder.Default
    private Map<Price, Integer> sellDeals = new HashMap<>();
    @Builder.Default
    private Queue<TradeLog> tradeLogs = new LinkedList<>();
    private int QuantityOfMarketBuy;
    private int QuantityOfMarketSell;
}
