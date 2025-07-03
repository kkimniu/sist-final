package io.cavia.trader.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ResponseEntity 에서 body에 담길 응답 형식을 통일하기 위한 객체
 * 응답은 아래와 같은 형식이 됨.
 * <p>
 * <pre><code> {
 *     "status": 200,
 *     "message": "OK",
 *     "data": {
 *         "id": 1,
 *         "username": "coding_partner",
 *         "email": "partner@example.com"
 *     }
 * }</code></pre>
 *
 * @author KimBeomhee
 */
@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final int status;
    private final String message;
    private final T data;
}
