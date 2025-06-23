package io.cavia.trader.module.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StocksOutput {

    // 고유번호
    private int id;

    // 종목명
    private String name;

    // 종목코드
    private String code;

    // 생성 시간
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
