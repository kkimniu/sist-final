package io.cavia.trader.module.game.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Member {
    private int id;
    private String email;
    private String nickname;
    private long cash;
    private int totalScore;
}
