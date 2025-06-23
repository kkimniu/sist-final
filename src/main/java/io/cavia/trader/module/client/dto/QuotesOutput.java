package io.cavia.trader.module.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("stock_id")
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
    @JsonProperty("askp_rsqn1")
    private Long askpRsqn1;
    @JsonProperty("askp_rsqn2")
    private Long askpRsqn2;
    @JsonProperty("askp_rsqn3")
    private Long askpRsqn3;
    @JsonProperty("askp_rsqn4")
    private Long askpRsqn4;
    @JsonProperty("askp_rsqn5")
    private Long askpRsqn5;
    @JsonProperty("askp_rsqn6")
    private Long askpRsqn6;
    @JsonProperty("askp_rsqn7")
    private Long askpRsqn7;
    @JsonProperty("askp_rsqn8")
    private Long askpRsqn8;
    @JsonProperty("askp_rsqn9")
    private Long askpRsqn9;
    @JsonProperty("askp_rsqn10")
    private Long askpRsqn10;

    // 각 호가별 잔량 (매수)
    @JsonProperty("bidp_rsqn1")
    private Long bidpRsqn1;
    @JsonProperty("bidp_rsqn2")
    private Long bidpRsqn2;
    @JsonProperty("bidp_rsqn3")
    private Long bidpRsqn3;
    @JsonProperty("bidp_rsqn4")
    private Long bidpRsqn4;
    @JsonProperty("bidp_rsqn5")
    private Long bidpRsqn5;
    @JsonProperty("bidp_rsqn6")
    private Long bidpRsqn6;
    @JsonProperty("bidp_rsqn7")
    private Long bidpRsqn7;
    @JsonProperty("bidp_rsqn8")
    private Long bidpRsqn8;
    @JsonProperty("bidp_rsqn9")
    private Long bidpRsqn9;
    @JsonProperty("bidp_rsqn10")
    private Long bidpRsqn10;

    // 총 매도호가 잔량 증감
    @JsonProperty("total_askp_rsqn_icdc")
    private Integer totalAskpRsqnIcdc;

    // 총 매수호가 잔량 증감
    @JsonProperty("total_bidp_rsqn_icdc")
    private Integer totalBidpRsqnIcdc;

    // 생성 시간
    @JsonProperty("created_at")
    private Timestamp createdAt;
}
