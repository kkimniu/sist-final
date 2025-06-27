package io.cavia.trader.module.game.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Member {
    private int id;
    private String email;
    private String nickname;
    private long cash;
    private int totalScore;
}
