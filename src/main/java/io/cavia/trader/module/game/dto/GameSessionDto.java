package io.cavia.trader.module.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class GameSessionDto {
    private Deque<GameDto> gameDtos = new ArrayDeque<>();
}
