package io.cavia.trader.common.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final int status;
    private final String message;
    private final T data;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 성공 응답을 위한 정적 팩토리 메서드
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "요청에 성공했습니다.", data);
    }

    // 데이터가 없는 성공 응답
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "요청에 성공했습니다.", null);
    }

    // 예외 응답을 위한 정적 팩토리 메서드
    public static <T> ApiResponse<T> error(int errorCode, String message) {
        return new ApiResponse<>(errorCode, message, null);
    }
}
