package io.cavia.trader.module.client.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Trades {

    // 고유번호
    private Long id;

    // 종목 id
    private Integer stockId;

    // 주식 현재가
    private Integer stckPrpr;

    // 매도호가1
    private Integer askp1;

    // 매수호가1
    private Integer bidp1;

    // 체결 거래량
    private Long cntgVol;

    // 정적VI발동기준가
    private Integer viStndPrc;

    // 생성시간
    private Timestamp createdAt;
}


