package io.cavia.trader.module.game.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ChatLog {
    private long memberId;
    private String memberNickname;
    private String msg;
    private LocalDateTime sentTime;
}
