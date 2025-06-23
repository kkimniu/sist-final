package io.cavia.trader.module.client.connector;

import io.cavia.trader.module.client.dto.QuotesDTO;
import io.cavia.trader.module.client.dto.StocksDTO;
import io.cavia.trader.module.client.dto.StocksOutput;
import io.cavia.trader.module.client.dto.TradesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestWebClientImpl implements RestWebClient {

    private final WebClient webClient;

    public Mono<StocksDTO> getStocks() {
        /**
         * 현재 DB에 저장된 모든 종목 리스트를 가진 Mono객체를 반환합니다.
         */
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/stocks")
                            .build())
                    .retrieve() // 요청 보내고, 성공하면 응답 바디를 가져올 준비를 하고, 실패하면 예외를 던짐
                    .bodyToMono(StocksDTO.class);
        } catch (WebClientResponseException e) {
            throw new RuntimeException("API 호출 중 에러 발생! Status Code: " + e.getStatusCode() + ", Response Body: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("알 수 없는 런타임에러 발생 " + e.getMessage(), e);
        }
    }

    public Mono<TradesDTO> getTrades(int stockId) {
        /**
         * 현재 DB에 저장된 체결 집계 데이터 중 인자로 주입된 stockId를 가지고 있는 Row의 집합을 가진 Mono객체를 반환합니다.
         */
        try {
            // 요청 보내고, 성공하면 응답 바디를 가져올 준비를 하고, 실패하면 예외를 던짐
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/stock/"+stockId+"/trades")
                            .build())
                    .retrieve() // 요청 보내고, 성공하면 응답 바디를 가져올 준비를 하고, 실패하면 예외를 던짐
                    .bodyToMono(TradesDTO.class);
        } catch (WebClientResponseException e) {
            throw new RuntimeException("API 호출 중 에러 발생! Status Code: " + e.getStatusCode() + ", Response Body: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("알 수 없는 런타임에러 발생 " + e.getMessage(), e);
        }
    }

    public Mono<QuotesDTO> getQuotes(int stockId) {
        /**
         * 현재 DB에 저장된 호가 집계 데이터 중 인자로 주입된 stockId를 가지고 있는 Row의 집합을 가진 Mono객체를 반환합니다.
         */
        try {
            // 요청 보내고, 성공하면 응답 바디를 가져올 준비를 하고, 실패하면 예외를 던짐
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/stock/"+stockId+"/quotes")
                            .build())
                    .retrieve() // 요청 보내고, 성공하면 응답 바디를 가져올 준비를 하고, 실패하면 예외를 던짐
                    .bodyToMono(QuotesDTO.class);
        } catch (WebClientResponseException e) {
            throw new RuntimeException("API 호출 중 에러 발생! Status Code: " + e.getStatusCode() + ", Response Body: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("알 수 없는 런타임에러 발생 " + e.getMessage(), e);
        }
    }
}
