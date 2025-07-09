package io.cavia.trader.module.notice.exception;

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

    @ExceptionHandler(NoticeOperationFailedException.class)
    public ResponseEntity<String> handleNoticeOperationFailed(NoticeOperationFailedException e) {
        log.error("공지사항 작업 실패", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("공지사항 작업 실패:" + e.getMessage());
    }

    @ExceptionHandler(InvalidNoticeRequestException.class)
    public ResponseEntity<String> handleInvalidNoticeRequest(InvalidNoticeRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
