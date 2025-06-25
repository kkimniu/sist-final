package io.cavia.trader.module.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 'Bearer '를 제외한 순수한 토큰을 가져옵니다.
        String token = jwtUtil.substringToken(request.getHeader(JwtUtil.AUTHORIZATION_HEADER));

        if (token != null) {
            // 토큰 유효성 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                Claims userInfo = jwtUtil.getUserInfoFromToken(token);
                String username = userInfo.getSubject();

                try {
                    // 사용자 정보로 인증 객체 만들기
                    setAuthentication(username);
                } catch (Exception e) {
                    log.error("인증 처리 중 예외 발생: {}", e.getMessage());
                    // response.sendError() 등으로 클라이언트에 에러 응답을 보낼 수 있습니다.
                }
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * 인증 처리 메서드
     *
     * @param username JWT에서 추출한 사용자 이름
     */
    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // UserDetailsService를 통해 사용자 정보를 로드합니다.
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 인증 객체를 생성합니다.
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // SecurityContext에 인증 정보를 설정합니다.
        context.setAuthentication(authentication);
        // SecurityContextHolder에 SecurityContext를 설정합니다.
        SecurityContextHolder.setContext(context);
    }
}