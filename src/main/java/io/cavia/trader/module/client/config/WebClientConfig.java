package io.cavia.trader.module.client.config;

import io.cavia.trader.module.client.connector.RestWebClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final WebClient.Builder webClientBuilder;

    private String baseUrl = "http://121.162.225.102:8081/homenet";

    @Bean
    public WebClient webClient() {
        return webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

}
