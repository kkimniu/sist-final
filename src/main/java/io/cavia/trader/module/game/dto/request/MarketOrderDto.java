package io.cavia.trader.module.game.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarketOrderDto {

    @NotNull(message = "주문 수량은 필수 입력사항입니다.")
    @Min(value = 1, message = "1 이상의 주문 수량을 입력하세요.")
    private Integer quantity;
}
