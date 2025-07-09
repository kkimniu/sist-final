package io.cavia.trader.module.notice.exception;

public class InvalidNoticeRequestException extends IllegalArgumentException {
    public InvalidNoticeRequestException(String message) {
        super(message);
    }
}