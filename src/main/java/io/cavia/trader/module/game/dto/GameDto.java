package io.cavia.trader.module.game.dto;

import io.cavia.trader.module.client.dto.QuotesOutput;
import io.cavia.trader.module.client.dto.TradesOutput;
import io.cavia.trader.module.game.entity.GameParticipation;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GameDto {
    private long id;
    private long stockId;
    private List<QuotesOutput> quotes;
    private List<TradesOutput> trades;
    private List<GameParticipation> gameParticipations;
    private Map<Long, WebSocketSession> chartSessions;
    private Map<String, Long> UserIdsInChartSessions;
    private Map<Long, WebSocketSession> chatSessions;
    private Map<String, Long> UserIdsInChatSessions;
    private LocalDateTime startedAt;
}
