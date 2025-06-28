package io.cavia.trader.module.auth.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    /**
     * 회원의 고유 식별자 (Primary Key, Auto Increment)
     */
    private Long id;

    /**
     * 회원의 이메일 주소 (Unique)
     */
    private String email;

    /**
     * 암호화된 회원의 비밀번호
     */
    private String password;

    /**
     * 회원의 닉네임 (Unique)
     */
    private String nickname;

    /**
     * 회원의 권한 (USER or ADMIN, 기본값: USER)
     */
    @Builder.Default
    private MemberRoleEnum role = MemberRoleEnum.USER;
    /**
     * 보유 캐시 (기본값: 0)
     */
    @Builder.Default
    private Long cash = 0L;

    /**
     * 누적 점수 (기본값: 0)
     */
    @Builder.Default
    private Integer totalScore = 0;

    /**
     * 계정 생성일
     */
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * 닉네임 마지막 변경일 (Nullable)
     */
    private LocalDateTime nicknameUpdatedAt;

    /**
     * 비밀번호 마지막 변경일 (Nullable)
     */
    private LocalDateTime passwordUpdatedAt;
}