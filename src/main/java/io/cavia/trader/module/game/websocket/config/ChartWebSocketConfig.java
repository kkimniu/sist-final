package io.cavia.trader.module.game.websocket.config;

import io.cavia.trader.module.game.websocket.handler.ChartWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class ChartWebSocketConfig implements WebSocketConfigurer {

    private final ChartWebSocketHandler chartWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chartWebSocketHandler, "/ws/chart").setAllowedOrigins("*");
    }
}
