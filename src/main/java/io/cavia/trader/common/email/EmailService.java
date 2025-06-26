package io.cavia.trader.common.email;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
}
