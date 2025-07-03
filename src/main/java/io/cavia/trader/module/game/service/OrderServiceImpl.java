package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.game.dto.OrderDto;
import io.cavia.trader.module.game.dto.OrderTableDto;
import io.cavia.trader.module.game.dto.PlayerStatusDto;
import io.cavia.trader.module.game.dto.request.CancelOrderDto;
import io.cavia.trader.module.game.dto.request.MarketOrderDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public boolean placeSellOrder(GameDto gameDto, OrderTableDto orderTableDto, long targetId) {
        if (gameDto.getPlayerStatusDtos().get(targetId).getStocksHolding() < orderTableDto.getQuantity()) {
            throw new RuntimeException("보유 중인 주식보다 많은 수량은 주문 불가능합니다.");
        } else {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);

            Queue<OrderTableDto> orders = playerStatusDto.getOrderDto().getOrderTableDto();
            orderTableDto.setId(orders.size() + 1);
            orderTableDto.setCreatedAt(LocalDateTime.now());
            orders.add(orderTableDto);

            return true;
        }
    }

    @Override
    public boolean placeBuyOrder(GameDto gameDto, OrderTableDto orderTableDto, long targetId) {
        if (gameDto.getPlayerStatusDtos().get(targetId).getEarnedCash() < (long) orderTableDto.getQuantity() * orderTableDto.getPrice()) {
            throw new RuntimeException("잔고가 부족합니다.");
        } else {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);

            Queue<OrderTableDto> orders = playerStatusDto.getOrderDto().getOrderTableDto();
            orderTableDto.setId(orders.size() + 1);
            orderTableDto.setCreatedAt(LocalDateTime.now());
            orders.add(orderTableDto);

            return true;
        }
    }

    @Override
    public void placeCancelOrder(GameDto gameDto, CancelOrderDto cancelOrderDto, long targetId) {
        OrderDto orderDto = gameDto.getPlayerStatusDtos().get(targetId).getOrderDto();
        AtomicBoolean isFinded = new AtomicBoolean(false);
        orderDto.getOrderTableDto().forEach(orderTableDto -> {
            if (orderTableDto.getId() == cancelOrderDto.getOrderId()){
                orderDto.getOrderTableDto().remove(orderTableDto);
                isFinded.set(true);
            }
        });
        if (isFinded.get()){
            return;
        }else {
            throw new RuntimeException("주문이 존재하지 않습니다.");
        }
    }

    @Override
    public void placeMarketSellOrder(GameDto gameDto, MarketOrderDto marketOrderDto, long targetId) {
        if (gameDto.getPlayerStatusDtos().get(targetId).getStocksHolding() < marketOrderDto.getQuantity()) {
            throw new RuntimeException("보유 중인 주식보다 많은 수량은 주문 불가능합니다.");
        } else {
            gameDto.getPlayerStatusDtos().get(targetId).getOrderDto().setQuantityOfMarketSell(marketOrderDto.getQuantity());
        }
    }

    @Override
    public void placeMarketBuyOrder(GameDto gameDto, MarketOrderDto marketOrderDto, long targetId) {
        // 아오.. 현재가가 또 필요함 ㅋㅋ
        // 10초마다 현재가가 업데이트 되는 필드 만듬
        if (gameDto.getPlayerStatusDtos().get(targetId).getEarnedCash() < (long) marketOrderDto.getQuantity() * gameDto.getCurrentPriceIn10Second()) {
            throw new RuntimeException("잔고가 부족합니다.");
        } else {
            gameDto.getPlayerStatusDtos().get(targetId).getOrderDto().setQuantityOfMarketBuy(marketOrderDto.getQuantity());
        }
    }
}
