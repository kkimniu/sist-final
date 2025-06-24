package io.cavia.trader.common.email;

public interface EmailService {

    void sendAuthEmail(String to, String authKey);
}
