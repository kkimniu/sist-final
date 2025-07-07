package io.cavia.trader.common.email;

public interface EmailService {

    /**
     * @param to      이메일 보낼 대상
     * @param subject 이메일 제목
     * @param body    html 형식의 이메일 본문
     */
    void sendEmail(String to, String subject, String body);
}
