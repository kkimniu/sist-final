package io.cavia.trader.module.game.dto;

import io.cavia.trader.module.client.dto.QuotesOutput;
import io.cavia.trader.module.client.dto.TradesOutput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GameDTO {
    private int id;
    private int stockId;
    private long userId;
    private List<QuotesOutput> quotes;
    private List<TradesOutput> trades;
    private List<UserDTO> userDTOs;
    private Map<Long, WebSocketSession> chartSessions;
    private Map<Long, WebSocketSession> chatSessions;
    private LocalDateTime startedAt;
}
