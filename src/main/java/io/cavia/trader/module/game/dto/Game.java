package io.cavia.trader.module.game.dto;

import io.cavia.trader.module.client.dto.QuotesOutput;
import io.cavia.trader.module.client.dto.TradesOutput;
import jakarta.websocket.Session;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Game {

    private int id;
    private int stockId;
    private List<QuotesOutput> quotes;
    private List<TradesOutput> trades;
    private Map<Integer, Session> chartSessions;
    private Map<Integer, Session> chatSessions;
    private LocalDateTime startedAt;
}
