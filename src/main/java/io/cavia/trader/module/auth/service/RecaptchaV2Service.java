package io.cavia.trader.module.auth.service;

import io.cavia.trader.module.auth.dto.RecaptchaResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class RecaptchaV2Service implements RecaptchaService {

    private final WebClient webClient;

    @Value("${recaptcha.secret-key:hahaha}")
    private String secretKey;

    private static final String GOOGLE_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public RecaptchaV2Service(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(GOOGLE_RECAPTCHA_VERIFY_URL).build();
    }

    @Override
    public boolean verifyRecaptcha(String recaptchaToken) {
        // TODO: 다른 팀원들 개발용으로 검증 없이 통과하도록 만듦. 릴리즈 버전에서 삭제예정
        if ("hahaha".equals(secretKey)) {
            log.warn("recaptcha.secret-key 프로퍼티 입력 안해서 리캡챠 토큰 검증 없이 통과, 개발테스트용");
            return true;
        }
        if (recaptchaToken == null || recaptchaToken.isEmpty()) {
            return false;
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("secret", secretKey);
        body.add("response", recaptchaToken);

        try {
            return Boolean.TRUE.equals(webClient.post()
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(RecaptchaResponseDto.class)
                    .map(responseDto -> responseDto.isSuccess())
                    .block());
        } catch (Exception e) {
            throw new RuntimeException("리캡챠 인증 중 오류 발생", e);
        }
    }
}

