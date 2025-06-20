package io.cavia.trader.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * ChatWebSocketHandler 등에서 예약 삭제 작업용으로 주입해서 사용하기 위해 만듬
 */
@Configuration
public class SchedulerConfig {
    @Bean
    public ScheduledExecutorService scheduler(){
        return Executors.newScheduledThreadPool(5);
    }
}
