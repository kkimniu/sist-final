package io.cavia.trader.module.member.security;

import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.entity.MemberRoleEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final Member member;

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Member 엔티티의 Role 정보를 기반으로 GrantedAuthority 컬렉션을 생성합니다.
        MemberRoleEnum role = member.getRole();
        String authority = role.getAuthority(); // "ROLE_USER", "ROLE_ADMIN"

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    // 아래 4개 메서드는 계정의 상태를 나타냅니다.
    // 특별한 로직이 없다면 모두 true를 반환하도록 설정할 수 있습니다.

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았는가?
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않았는가?
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호)이 만료되지 않았는가?
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화되었는가?

    }
}

