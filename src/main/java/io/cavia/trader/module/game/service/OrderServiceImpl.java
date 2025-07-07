package io.cavia.trader.module.game.service;

import io.cavia.trader.common.exception.ApiException;
import io.cavia.trader.common.exception.ErrorCode;
import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.game.dto.OrderDto;
import io.cavia.trader.module.game.dto.OrderTableDto;
import io.cavia.trader.module.game.dto.PlayerStatusDto;
import io.cavia.trader.module.game.dto.request.CancelOrderDto;
import io.cavia.trader.module.game.dto.request.MarketOrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public boolean placeSellOrder(GameDto gameDto, OrderTableDto orderTableDto, long targetId) {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);
            Queue<OrderTableDto> orders = playerStatusDto.getOrderDto().getOrderTableDtos();
            if(orders.size() >= 10) {
                throw new ApiException(ErrorCode.TOO_MANY_ORDERS);
            }

            int totalQuantity = orderTableDto.getQuantity();
            for (OrderTableDto dto : orders) {
                if (dto.getPrice() < 0) {
                    totalQuantity += dto.getQuantity();
                }
            }
            orderTableDto.setQuantity(totalQuantity);

            if (gameDto.getPlayerStatusDtos().get(targetId).getStocksHolding() <
                    orderTableDto.getQuantity()) {
                throw new ApiException(ErrorCode.NOT_ENOUGH_SHARES_HELD);
            } else {
                orderTableDto.setId(String.format("%06d", playerStatusDto.getIdCreator().getAndIncrement() % 1000000));
                orderTableDto.setCreatedAt(LocalDateTime.now());
                orders.add(orderTableDto);
                playerStatusDto.setUpdated(true);
                return true;
            }
    }

    @Override
    public boolean placeBuyOrder(GameDto gameDto, OrderTableDto orderTableDto, long targetId) {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);
            Queue<OrderTableDto> orders = playerStatusDto.getOrderDto().getOrderTableDtos();
            if(orders.size() >= 10) {
                throw new ApiException(ErrorCode.TOO_MANY_ORDERS);
            }
            int totalQuantity = orderTableDto.getQuantity();
            for (OrderTableDto dto : orders) {
                if (dto.getPrice() > 0) {
                    totalQuantity += dto.getQuantity();
                }
            }
            orderTableDto.setQuantity(totalQuantity);

            if (gameDto.getPlayerStatusDtos().get(targetId).getEarnedCash() < (long) orderTableDto.getQuantity() * orderTableDto.getPrice()) {
                throw new ApiException(ErrorCode.NOT_ENOUGH_BALANCE);
            } else {
                orderTableDto.setId(String.format("%06d", playerStatusDto.getIdCreator().getAndIncrement() % 1000000));
                orderTableDto.setCreatedAt(LocalDateTime.now());
                orders.add(orderTableDto);
                playerStatusDto.setUpdated(true);
                return true;
            }
    }

    @Override
    public void placeCancelOrder(GameDto gameDto, CancelOrderDto cancelOrderDto, long targetId) {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);
            OrderDto orderDto = playerStatusDto.getOrderDto();
            AtomicBoolean isFinded = new AtomicBoolean(false);
            orderDto.getOrderTableDtos().forEach(orderTableDto -> {
                if (orderTableDto.getId().equals(cancelOrderDto.getOrderId())) {
                    orderDto.getOrderTableDtos().remove(orderTableDto);
                    isFinded.set(true);
                    playerStatusDto.setUpdated(true);
                }
            });
            if (!isFinded.get()) {
                throw new ApiException(ErrorCode.ORDER_NOT_FOUND);
            }
    }

    @Override
    public void placeMarketSellOrder(GameDto gameDto, MarketOrderDto marketOrderDto, long targetId) {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);
            if (playerStatusDto.getStocksHolding() <
                    marketOrderDto.getQuantity() + playerStatusDto.getOrderDto().getQuantityOfMarketSell()) {
                throw new ApiException(ErrorCode.NOT_ENOUGH_SHARES_HELD);
            } else {
                playerStatusDto.getOrderDto().setQuantityOfMarketSell(
                        playerStatusDto.getOrderDto().getQuantityOfMarketSell() +
                                marketOrderDto.getQuantity()
                );
                playerStatusDto.setUpdated(true);
            }
    }

    @Override
    public void placeMarketBuyOrder(GameDto gameDto, MarketOrderDto marketOrderDto, long targetId) {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);
            // 아오.. 현재가가 또 필요함 ㅋㅋ
            if (playerStatusDto.getEarnedCash() < (long) (marketOrderDto.getQuantity()
                    + playerStatusDto.getOrderDto().getQuantityOfMarketBuy())
                    * gameDto.getCurrentPrice()) {
                throw new ApiException(ErrorCode.NOT_ENOUGH_BALANCE);
            } else {
                playerStatusDto.getOrderDto().setQuantityOfMarketBuy(
                        playerStatusDto.getOrderDto().getQuantityOfMarketBuy() +
                                marketOrderDto.getQuantity()
                );
                playerStatusDto.setUpdated(true);
            }
    }
}
