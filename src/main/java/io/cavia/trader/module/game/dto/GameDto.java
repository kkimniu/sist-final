package io.cavia.trader.module.game.dto;

import io.cavia.trader.module.client.dto.QuotesOutput;
import io.cavia.trader.module.client.dto.TradesOutput;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Component
public class GameDto {
    private long id;
    private long stockId;
    private int CurrentPriceIn10Second;
    private List<QuotesOutput> quotes;
    private List<TradesOutput> trades;
    private Map<Long, PlayerStatusDto> playerStatusDtos;
    private Map<Long, WebSocketSession> chartSessions;
    private Map<String, Long> userIdsInChartSessions;
    private Map<Long, WebSocketSession> chatSessions;
    private Map<String, Long> userIdsInChatSessions;
    private Queue<ChatLog> chatLogs;
    private LocalDateTime startedAt;
}
