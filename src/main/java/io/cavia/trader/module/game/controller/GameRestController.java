package io.cavia.trader.module.game.controller;

import io.cavia.trader.common.exception.ApiException;
import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.game.dto.GameDto;
import io.cavia.trader.module.game.dto.GameSessionDto;
import io.cavia.trader.module.game.dto.OrderTableDto;
import io.cavia.trader.module.game.dto.request.CancelOrderDto;
import io.cavia.trader.module.game.dto.request.MarketOrderDto;
import io.cavia.trader.module.game.service.GameAdministrationService;
import io.cavia.trader.module.game.service.GameManager;
import io.cavia.trader.module.game.service.OrderService;
import io.cavia.trader.module.member.entity.GameParticipation;
import io.cavia.trader.module.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
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
    private final GameAdministrationService gameAdministrationService;

    private GameDto gameDto;

    @PostMapping("/verify")
    public ResponseEntity<?> validateAccess(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            return new ResponseEntity<Member>(userDetails.getMember(), HttpStatus.OK);
        } catch (ApiException e) {
            throw e;
        }
    }

    @PatchMapping("/buy")
    public ResponseEntity<?> placeBuyOrder(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody OrderTableDto orderTableDto, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            for (GameDto game : gameSessionDto.getGameDtos()) {
                if (game.getPlayerStatusDtos().containsKey(userDetails.getMember().getId())) {
                    orderService.placeBuyOrder(game, orderTableDto, userDetails.getMember().getId());
                    return ResponseEntity.status(200).body("주문 완료");
                }
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유저를 게임에서 찾을 수 없습니다.");
        } catch (ResponseStatusException e) {
            throw e;
        }
    }

    @PatchMapping("/sell")
    public ResponseEntity<?> placeSellOrder(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody OrderTableDto orderTableDto, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            orderTableDto.setPrice(-Math.abs(orderTableDto.getPrice()));
            for (GameDto game : gameSessionDto.getGameDtos()) {
                if (game.getPlayerStatusDtos().containsKey(userDetails.getMember().getId())) {
                    orderService.placeSellOrder(game, orderTableDto, userDetails.getMember().getId());
                    return ResponseEntity.status(200).body("주문 완료");
                }
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유저를 게임에서 찾을 수 없습니다.");
        } catch (ResponseStatusException e) {
            throw e;
        }
    }

    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody CancelOrderDto cancelOrderDto) {
        try {
            for (GameDto game : gameSessionDto.getGameDtos()) {
                if (game.getPlayerStatusDtos().containsKey(userDetails.getMember().getId())) {
                    orderService.placeCancelOrder(game, cancelOrderDto, userDetails.getMember().getId());
                    return ResponseEntity.status(200).body("주문 취소 완료");
                }
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유저를 게임에서 찾을 수 없습니다.");
        } catch (ResponseStatusException e) {
            throw e;
        }
    }

    @PatchMapping("/market-buy")
    public ResponseEntity<?> marketBuy(@AuthenticationPrincipal UserDetailsImpl
                                               userDetails, @Valid @RequestBody MarketOrderDto marketOrderDto) {
        try {
            for (GameDto game : gameSessionDto.getGameDtos()) {
                if (game.getPlayerStatusDtos().containsKey(userDetails.getMember().getId())) {
                    orderService.placeMarketBuyOrder(game, marketOrderDto, userDetails.getMember().getId());
                    return ResponseEntity.status(200).body("주문 완료");
                }
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유저를 게임에서 찾을 수 없습니다.");
        } catch (ResponseStatusException e) {
            throw e;
        }
    }

    @PatchMapping("/market-sell")
    public ResponseEntity<?> marketSell(@AuthenticationPrincipal UserDetailsImpl
                                                userDetails, @Valid @RequestBody MarketOrderDto marketOrderDto) {
        try {
            for (GameDto game : gameSessionDto.getGameDtos()) {
                if (game.getPlayerStatusDtos().containsKey(userDetails.getMember().getId())) {
                    orderService.placeMarketSellOrder(game, marketOrderDto, userDetails.getMember().getId());
                    return ResponseEntity.status(200).body("주문 완료");
                }
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유저를 게임에서 찾을 수 없습니다.");
        } catch (ResponseStatusException e) {
            throw e;
        }
    }

    @GetMapping("/last-game-participation")
    public ResponseEntity<?> getLastGameParticipation(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            long memberId = userDetails.getMember().getId();
            return new ResponseEntity<GameParticipation>(
                    gameAdministrationService.getLastGameParticipation(
                            userDetails.getMember().getId()
                    ), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조회되는 게임 기록이 없습니다.");
        }

    }


}
