package io.cavia.trader.module.notice.excetion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * notice에서만 사용하기 위해서 만든 예외처리
 *
 * @RestControllerAdvice(basePackages = "io.cavia.trader.module.notice")
 * IllegalArgumentException을 사용한 이유는 postman에서 잘못되게 입력할 경우를 위해 예외처리를 위해서 사용함
 * NoticeSaveFailedException 커스텀 예외처리를 만든 이유는 좀 더쉽게 저장에서 예외처리를 한지 알기위해서 사용함
 */
@Slf4j
@RestControllerAdvice(basePackages = "io.cavia.trader.module.notice")
public class NoticeExceptionHandler {

    @ExceptionHandler(InvalidNoticeTitleException.class)
    public ResponseEntity<String> handleInvalidNoticeTitle(InvalidNoticeTitleException e) {
        log.warn("title 입력 오류", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("title 입력 오류:" + e.getMessage());
    }

    @ExceptionHandler(InvalidNoticeContentException.class)
    public ResponseEntity<String> handleInvalidNoticeContent(InvalidNoticeContentException e) {
        log.warn("content 입력 오류", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("content 입력 오류:" + e.getMessage());
    }

    @ExceptionHandler(NoticeSaveFailedException.class)
    public ResponseEntity<String> handleNoticeSaveFailed(NoticeSaveFailedException e) {
        log.error("공지사항 저장 실패", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("공지사항 저장 실패:" + e.getMessage());
    }
}
