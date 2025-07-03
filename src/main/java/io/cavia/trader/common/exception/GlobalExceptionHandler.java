package io.cavia.trader.common.exception;

import io.cavia.trader.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 프로젝트 전역에서 발생하는 모든 예외를 처리하는 클래스
 *
 * @author KimBeomhee
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 우리가 직접 정의한 비즈니스 예외(ApiException)를 처리합니다.
     *
     * @param e ApiException
     * @return ApiResponse가 담긴 ResponseEntity
     */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ApiResponse<?>> handleCustomException(ApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("Business Exception Occurred: {}", e.getMessage());

        ApiResponse<Object> responseBody = ApiResponse.error(
                errorCode.getHttpStatus().value(),
                errorCode.getMessage()
        );

        return new ResponseEntity<>(responseBody, errorCode.getHttpStatus());
    }

    /**
     * 위에서 처리하지 못한 모든 예외를 처리합니다.
     *
     * @param e Exception
     * @return ApiResponse가 담긴 ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error("Unhandled Exception Occurred", e);

        ApiResponse<Object> responseBody = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );

        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}