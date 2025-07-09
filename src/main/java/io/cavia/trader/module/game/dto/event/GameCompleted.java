package io.cavia.trader.module.game.dto.event;

import io.cavia.trader.module.game.dto.GameDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameCompleted implements Event{

    private GameDto gameDto;
}
