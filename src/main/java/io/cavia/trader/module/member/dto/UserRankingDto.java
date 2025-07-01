package io.cavia.trader.module.member.dto;

import lombok.*;

@Getter
public class UserRankingDto {
    private Long id;
    private String nickname;
    private Long cash;
    private Integer totalScore;
}
