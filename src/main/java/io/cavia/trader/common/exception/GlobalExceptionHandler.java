package io.cavia.trader.common.exception;

import io.cavia.trader.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
     * {@code @Valid} 어노테이션 유효성 검사 실패 시, 첫 번째 에러 메시지만을 응답합니다.
     *
     * @param e MethodArgumentNotValidException
     * @return ApiResponse가 담긴 ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException e) {

        ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        ApiResponse<Object> responseBody = ApiResponse.error(
                errorCode.getHttpStatus().value(),
                errorMessage // ⬅️ 상세 에러 메시지로 대체
        );
        return new ResponseEntity<>(responseBody, errorCode.getHttpStatus());
    }

    /**
     * 서블릿에서 없는 리소스를 요청하면 발생하는 예외를 처리합니다.
     * ex)No static resource images/logo.png
     *
     * @param e NoResourceFoundException
     * @return ApiResponse가 담긴 ResponseEntity
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(NoResourceFoundException e) {

        ApiResponse<Object> responseBody = ApiResponse.error(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    /**
     * 이외의 처리하지 못한 모든 예외를 처리합니다.
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