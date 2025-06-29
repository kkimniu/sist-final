package io.cavia.trader.module.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRoleEnum {
    USER("ROLE_USER"),  // 사용자 권한
    ADMIN("ROLE_ADMIN"); // 관리자 권한

    // 시큐리티 UserDetailsImpl 에서 사용
    private final String authority;
}
