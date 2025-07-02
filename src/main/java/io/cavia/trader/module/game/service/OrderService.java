package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.game.dto.OrderDto;
import io.cavia.trader.module.game.dto.OrderTableDto;
import io.cavia.trader.module.game.dto.request.CancelOrderDto;
import io.cavia.trader.module.game.dto.request.MarketOrderDto;

public interface OrderService {

    boolean placeBuyOrder(GameDto gameDto, OrderTableDto orderTableDto, long targetId);
    boolean placeSellOrder(GameDto gameDto, OrderTableDto orderTableDto, long targetId);
    void placeCancelOrder(GameDto gameDto, CancelOrderDto cancelOrderDto, long targetId);
    void placeMarketBuyOrder(GameDto gameDto, MarketOrderDto marketOrderDto, long targetId);
    void placeMarketSellOrder(GameDto gameDto, MarketOrderDto marketOrderDto, long targetId);
}
