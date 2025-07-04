package io.cavia.trader.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * ApiResponse 는 api의 응답 객체이고,
 * 이 클래스는 응답 객체를 ResponseEntity와 결합해 보일러 플레이트를 없애고, 쉽게 반환할 수 있도록 하는 헬퍼 클래스임.
 *
 * @author KimBeomhee
 * @see ApiResponse
 */
public final class ApiResponses {

    public static ResponseEntity<ApiResponse<?>> ok() {
        return of(HttpStatus.OK, "OK", null);
    }

    public static <T> ResponseEntity<ApiResponse<?>> ok(T data) {
        return of(HttpStatus.OK, "OK", data);
    }

    public static <T> ResponseEntity<ApiResponse<?>> ok(String message, T data) {
        return of(HttpStatus.OK, message, data);
    }

    public static ResponseEntity<ApiResponse<?>> created(URI location) {
        return created(location, "Created", null);
    }

    public static <T> ResponseEntity<ApiResponse<?>> created(URI location, T data) {
        return created(location, "Created", data);
    }

    public static <T> ResponseEntity<ApiResponse<?>> created(URI location, String message, T data) {
        return ResponseEntity
                .created(location)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), message, data));
    }

    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    public static ResponseEntity<ApiResponse<?>> of(HttpStatus httpStatus, String message) {
        return of(httpStatus, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<?>> of(HttpStatus httpStatus, String message, T data) {
        return new ResponseEntity<>(new ApiResponse<>(httpStatus.value(), message, data), httpStatus);
    }
}
