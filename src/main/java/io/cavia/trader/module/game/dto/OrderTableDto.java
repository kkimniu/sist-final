package io.cavia.trader.module.game.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderTableDto {
    private long id;
    @NotNull(message = "주문가격은 필수 입력사항입니다.")
    private Integer price;
    @NotNull(message = "주문수량은 필수 입력사항입니다.")
    @Min(value = 1, message = "1 이상의 주문수량을 입력해주세요.")
    private Integer quantity;
    private LocalDateTime createdAt;
}
