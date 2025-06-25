package io.cavia.trader.module.member.mypage.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class GameParticipationDto {
    private Long id;
    private int memberId;
    private int gameId;
    private float returnId;
    private int gameRank;
    private int earnedScore;
    private int postScore;
    private Long earnedCash;
    private Long postCash;
    private LocalDateTime enteredAt;
}
