package io.cavia.trader.module.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
public class QuotesOutput {

    // 고유번호
    private Long id;

    // 종목 id
    private Integer stockId;

    // 매도호가 1~10
    private Integer askp1;
    private Integer askp2;
    private Integer askp3;
    private Integer askp4;
    private Integer askp5;
    private Integer askp6;
    private Integer askp7;
    private Integer askp8;
    private Integer askp9;
    private Integer askp10;

    // 매수호가 1~10
    private Integer bidp1;
    private Integer bidp2;
    private Integer bidp3;
    private Integer bidp4;
    private Integer bidp5;
    private Integer bidp6;
    private Integer bidp7;
    private Integer bidp8;
    private Integer bidp9;
    private Integer bidp10;

    // 각 호가별 잔량 (매도)
    private Long askpRsqn1;
    private Long askpRsqn2;
    private Long askpRsqn3;
    private Long askpRsqn4;
    private Long askpRsqn5;
    private Long askpRsqn6;
    private Long askpRsqn7;
    private Long askpRsqn8;
    private Long askpRsqn9;
    private Long askpRsqn10;

    // 각 호가별 잔량 (매수)
    private Long bidpRsqn1;
    private Long bidpRsqn2;
    private Long bidpRsqn3;
    private Long bidpRsqn4;
    private Long bidpRsqn5;
    private Long bidpRsqn6;
    private Long bidpRsqn7;
    private Long bidpRsqn8;
    private Long bidpRsqn9;
    private Long bidpRsqn10;

    // 총 매도호가 잔량 증감
    private Integer totalAskpRsqnIcdc;

    // 총 매수호가 잔량 증감
    private Integer totalBidpRsqnIcdc;

    // 생성 시간
    private Timestamp createdAt;
}
