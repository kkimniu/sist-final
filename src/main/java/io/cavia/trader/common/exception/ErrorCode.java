package io.cavia.trader.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ========== 400 BAD REQUEST ==========
    /**
     * 비밀번호 불일치 (회원정보 변경 시)
     * AuthService의 login과 구분하기 위해 400으로 설정
     */
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    EXPIRED_AUTH_KEY(HttpStatus.BAD_REQUEST, "인증키가 만료되었습니다"),
    INVALID_AUTH_KEY(HttpStatus.BAD_REQUEST, "인증키가 일치하지 않습니다"),

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 파라미터가 유효하지 않습니다"),

    // ========== 401 UNAUTHORIZED ==========
    /**
     * 로그인 실패
     */
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다"),


    // ========== 403 FORBIDDEN ==========
    USER_NOT_IN_SESSION(HttpStatus.FORBIDDEN, "세션에서 해당 유저를 찾을 수 없습니다."),

    // ========== 404 NOT FOUND ==========
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    EMAIL_VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일의 인증 정보를 찾을 수 없습니다"),
    GAME_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "게임 참여 이력을 찾을 수 없습니다"),
    USER_RANKING_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 랭킹 정보를 찾을 수 없습니다"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다"),


    // ========== 409 CONFLICT ==========
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다"),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다"),


    // ========== 422 UNPROCESSABLE ENTITY ==========
    NOT_ENOUGH_BALANCE(HttpStatus.UNPROCESSABLE_ENTITY, "잔고가 부족합니다"),
    NOT_ENOUGH_SHARES_HELD(HttpStatus.UNPROCESSABLE_ENTITY, "주식 보유량이 부족합니다"),


    // ========== 429 TOO MANY REQUESTS ==========
    TOO_MANY_ORDERS(HttpStatus.TOO_MANY_REQUESTS, "주문 건수가 초과되었습니다. 미체결주문을 취소해주세요."),


    // ========== 500 INTERNAL SERVER ERROR ==========
    /**
     * DB UPDATE/DELETE 실패 등 예측하지 못한 서버 에러
     */
    DATABASE_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 작업에 실패했습니다"),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 발송에 실패했습니다");


    private final HttpStatus httpStatus;
    private final String message;
}