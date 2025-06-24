package io.cavia.trader.module.client.config;

import io.cavia.trader.module.client.connector.RestWebClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final WebClient.Builder webClientBuilder;

    private String baseUrl = "http://121.162.225.102:8081/homenet";

    @Bean
    public WebClient webClient() {
        // 대용량 문자열을 디코딩 하기 위해 WebClient의 응답 버퍼 사이즈 10mb로 설정
        return webClientBuilder
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(10 * 1024 * 1024)
                        )
                        .build()
                )
                .baseUrl(baseUrl)
                .build();
    }

}
