package io.cavia.trader.module.notice.excetion;

/**
 * 400대 에러로 title(제목)값이 잘못되면 발생하는 예외처리
 */
public class InvalidNoticeTitleException extends IllegalArgumentException {
    public InvalidNoticeTitleException(String message) {
        super(message);
    }
}
