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
        try {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);
            Queue<OrderTableDto> orders = playerStatusDto.getOrderDto().getOrderTableDtos();
            int totalQuantity = orderTableDto.getQuantity();
            for (OrderTableDto dto : orders) {
                if (dto.getPrice() < 0) {
                    totalQuantity += dto.getQuantity();
                }
            }
            orderTableDto.setQuantity(totalQuantity);

            if (gameDto.getPlayerStatusDtos().get(targetId).getStocksHolding() <
                    orderTableDto.getQuantity()) {
                throw new RuntimeException("주식보유량이 부족합니다.");
            } else {


                orderTableDto.setId(String.format("%06d", playerStatusDto.getIdCreator().getAndIncrement() % 1000000));
                orderTableDto.setCreatedAt(LocalDateTime.now());
                orders.add(orderTableDto);
                playerStatusDto.setUpdated(true);
                return true;

            }
        } catch (Exception e) {
            throw new RuntimeException("매도주문처리 중 오류 발생!!!", e);
        }
    }

    @Override
    public boolean placeBuyOrder(GameDto gameDto, OrderTableDto orderTableDto, long targetId) {
        try {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);
            Queue<OrderTableDto> orders = playerStatusDto.getOrderDto().getOrderTableDtos();
            int totalQuantity = orderTableDto.getQuantity();
            for (OrderTableDto dto : orders) {
                if (dto.getPrice() > 0) {
                    totalQuantity += dto.getQuantity();
                }
            }
            orderTableDto.setQuantity(totalQuantity);

            if (gameDto.getPlayerStatusDtos().get(targetId).getEarnedCash() < (long) orderTableDto.getQuantity() * orderTableDto.getPrice()) {
                throw new RuntimeException("잔고가 부족합니다.");
            } else {
                orderTableDto.setId(String.format("%06d", playerStatusDto.getIdCreator().getAndIncrement() % 1000000));
                orderTableDto.setCreatedAt(LocalDateTime.now());
                orders.add(orderTableDto);
                playerStatusDto.setUpdated(true);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("매수주문 처리 중 오류 발생!!!", e);
        }
    }

    @Override
    public void placeCancelOrder(GameDto gameDto, CancelOrderDto cancelOrderDto, long targetId) {
        try {
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
            if (isFinded.get()) {
            } else {
                throw new RuntimeException("주문이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("주문취소처리 중 오류 발생!!!", e);
        }
    }

    @Override
    public void placeMarketSellOrder(GameDto gameDto, MarketOrderDto marketOrderDto, long targetId) {
        try {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);
            if (playerStatusDto.getStocksHolding() <
                    marketOrderDto.getQuantity() + playerStatusDto.getOrderDto().getQuantityOfMarketSell()) {
                throw new RuntimeException("주식보유량이 부족합니다.");
            } else {
                playerStatusDto.getOrderDto().setQuantityOfMarketSell(
                        playerStatusDto.getOrderDto().getQuantityOfMarketSell() +
                                marketOrderDto.getQuantity()
                );
                playerStatusDto.setUpdated(true);
            }
        } catch (Exception e) {
            throw new RuntimeException("시장가매도주문 처리 중 오류 발생!!!", e);
        }
    }

    @Override
    public void placeMarketBuyOrder(GameDto gameDto, MarketOrderDto marketOrderDto, long targetId) {
        try {
            PlayerStatusDto playerStatusDto = gameDto.getPlayerStatusDtos().get(targetId);
            // 아오.. 현재가가 또 필요함 ㅋㅋ
            if (playerStatusDto.getEarnedCash() < (long) (marketOrderDto.getQuantity()
                    + playerStatusDto.getOrderDto().getQuantityOfMarketBuy())
                    * gameDto.getCurrentPrice()) {
                throw new RuntimeException("잔고가 부족합니다.");
            } else {
                playerStatusDto.getOrderDto().setQuantityOfMarketBuy(
                        playerStatusDto.getOrderDto().getQuantityOfMarketBuy() +
                                marketOrderDto.getQuantity()
                );
                playerStatusDto.setUpdated(true);
            }
        } catch (Exception e) {
            throw new RuntimeException("시장가매수주문 처리 중 오류 발생!!!", e);
        }
    }
}
