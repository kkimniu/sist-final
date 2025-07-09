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
public class TradesOutput {

    // 고유번호
    private Long id;

    // 종목 id
    @JsonProperty("stock_id")
    private Integer stockId;

    // 주식 현재가
    @JsonProperty("stck_prpr")
    private Integer stckPrpr;

    // 매도호가1
    private Integer askp1;

    // 매수호가1
    private Integer bidp1;

    // 체결 거래량
    @JsonProperty("cntg_vol")
    private Long cntgVol;

    // 정적VI발동기준가
    @JsonProperty("vi_stnd_prc")
    private Integer viStndPrc;

    // 생성시간
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}


