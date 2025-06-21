package io.cavia.trader.module.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Stocks {

    // 고유번호
    private int id;

    // 종목명
    private String name;

    // 종목코드
    private String code;

    // 생성 시간
    private Timestamp createdAt;
}
