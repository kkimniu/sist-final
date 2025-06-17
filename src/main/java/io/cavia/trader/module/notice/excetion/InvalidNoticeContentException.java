package io.cavia.trader.module.notice.excetion;

/**
 * 400대 에러로 Content(내용)이 잘못된 발상하는 예외처리
 */
public class InvalidNoticeContentException extends IllegalArgumentException {
    public InvalidNoticeContentException(String message) {
        super(message);
    }
}
