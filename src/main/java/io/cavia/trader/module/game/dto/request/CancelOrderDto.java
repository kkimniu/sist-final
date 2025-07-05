package io.cavia.trader.module.game.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CancelOrderDto {
    @NotNull(message = "주문 아이디는 필수 입력사항입니다.")
    private String orderId;
}
