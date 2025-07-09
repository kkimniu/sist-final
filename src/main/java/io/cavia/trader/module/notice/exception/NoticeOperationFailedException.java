package io.cavia.trader.module.notice.exception;

/**
 * 500 에러로 공지사항 저장이 실패했는데 알아보기 쉽게 하기위해서
 * 따로 커스텀 예외처리를 만듬
 */
public class NoticeOperationFailedException extends RuntimeException {
    public NoticeOperationFailedException(String message) {
        super(message);
    }
}
