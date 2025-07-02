package io.cavia.trader.module.game.controller;

import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.game.dto.GameSessionDto;
import io.cavia.trader.module.game.dto.OrderTableDto;
import io.cavia.trader.module.game.dto.request.CancelOrderDto;
import io.cavia.trader.module.game.dto.request.MarketOrderDto;
import io.cavia.trader.module.game.dto.response.ResponseDto;
import io.cavia.trader.module.game.service.GameManager;
import io.cavia.trader.module.game.service.OrderService;
import io.cavia.trader.module.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameRestController {
    /**
     * 사용자가 게임 페이지에서 보내는 요청들을 처리하는 RestApi입니다
     * validateAccess(String authorizationHeader) 사용자가 토큰을 포함해 요청을 보내면 인증 후 개인 정보가 담긴 Cliaims 객체를 반환합니다.
     */

    private final GameManager gameManager;
    private final GameSessionDto gameSessionDto;
    private final OrderService orderService;

    private GameDto gameDto;

    @PostMapping("/verify")
    public ResponseEntity<?> validateAccess(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            return new ResponseEntity<Member>(userDetails.getMember(), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/stocks-holding")
    public ResponseEntity<?> getStocksHolding(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            long memberId = userDetails.getMember().getId();
            return new ResponseEntity<ResponseDto>(
                    new ResponseDto("" + gameManager.findGameSessionByUserId(memberId)
                            .getPlayerStatusDtos().get(memberId).getStocksHolding()), HttpStatus.OK);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/buy")
    public ResponseEntity<?> placeBuyOrder(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody OrderTableDto orderTableDto) {
        gameSessionDto.getGameDtos().forEach(game -> {
            if (game.getPlayerStatusDtos().containsKey(userDetails.getMember().getId())) {
                orderService.placeBuyOrder(game, orderTableDto, userDetails.getMember().getId());
            }
            ;
        });
        return ResponseEntity.status(200).body("주문 완료");
    }

    @PatchMapping("/sell")
    public ResponseEntity<?> placeSellOrder(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody OrderTableDto orderTableDto) {
        orderTableDto.setPrice(-Math.abs(orderTableDto.getPrice()));
        gameSessionDto.getGameDtos().forEach(game -> {
            if (game.getPlayerStatusDtos().containsKey(userDetails.getMember().getId())) {
                orderService.placeSellOrder(game, orderTableDto, userDetails.getMember().getId());
            }
            ;
        });
        return ResponseEntity.status(200).body("주문 완료");
    }

    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody CancelOrderDto cancelOrderDto) {
        gameSessionDto.getGameDtos().forEach(game -> {
            if (game.getPlayerStatusDtos().containsKey(userDetails.getMember().getId())) {
                orderService.placeCancelOrder(game, cancelOrderDto, userDetails.getMember().getId());
            }
        });
        return ResponseEntity.status(200).body("주문 취소 완료");
    }

    @PatchMapping("/market-buy")
    public ResponseEntity<?> marketBuy(@AuthenticationPrincipal UserDetailsImpl
                                               userDetails, @Valid @RequestBody MarketOrderDto marketOrderDto) {
        gameSessionDto.getGameDtos().forEach(game -> {
            if (game.getPlayerStatusDtos().containsKey(userDetails.getMember().getId())) {
                orderService.placeMarketBuyOrder(game, marketOrderDto, userDetails.getMember().getId());
            }
        });
        return ResponseEntity.status(200).body("주문 완료");
    }

    @PatchMapping("/market-sell")
    public ResponseEntity<?> marketSell(@AuthenticationPrincipal UserDetailsImpl
                                                userDetails, @Valid @RequestBody MarketOrderDto marketOrderDto) {
        gameSessionDto.getGameDtos().forEach(game -> {
            if (game.getPlayerStatusDtos().containsKey(userDetails.getMember().getId())) {
                orderService.placeMarketSellOrder(game, marketOrderDto, userDetails.getMember().getId());
            }
        });
        return ResponseEntity.status(200).body("주문 완료");
    }
}
