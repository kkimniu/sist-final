package io.cavia.trader.common.config;

import io.cavia.trader.module.jwt.JwtAuthenticationFilter;
import io.cavia.trader.module.jwt.JwtUtil;
import io.cavia.trader.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    // PasswordEncoder를 Bean으로 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // JwtAuthenticationFilter를 Bean으로 등록
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, memberRepository);
    }

    // SecurityFilterChain 설정을 Bean으로 등록
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 비활성화
        http.csrf((csrf) -> csrf.disable());

        // 세션 관리 방식을 STATELESS(상태 비저장)으로 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 요청에 대한 접근 권한 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        // [ 1. 비회원(Anonymous) 접근 허용 ]
                        .requestMatchers("/", "/login", "/signup", "/signup/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/login-checker").permitAll()

                        // [ 2. 회원(USER, ADMIN) 접근 허용 ]
                        .requestMatchers("/api/").authenticated()

                        // [ 3. 관리자(ADMIN)만 접근 허용 ]
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // [ 4. 그 외 모든 요청 ]
                        // 위에서 설정한 경로 외의 모든 요청은 인증된 사용자만 접근할 수 있습니다.
                        .anyRequest().authenticated()
        );

        // 커스텀 필터인 JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}