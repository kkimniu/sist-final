package io.cavia.trader.module.auth.service;

public interface RecaptchaService {

    /**
     * reCAPTCHA 토큰을 구글 서버에 보내 검증합니다.
     *
     * @param recaptchaToken 클라이언트에서 받은 reCAPTCHA 토큰
     * @return true/false reCAPTCHA 토컨의 검증 여부
     */
    boolean verifyRecaptcha(String recaptchaToken);
}
