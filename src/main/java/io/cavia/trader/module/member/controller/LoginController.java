package io.cavia.trader.module.member.controller;

import io.cavia.trader.module.jwt.JwtUtil;
import io.cavia.trader.module.member.dto.LoginRequestDto;
import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.security.UserDetailsImpl;
import io.cavia.trader.module.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "member/login";
    }

    @GetMapping("login-checker")
    public String showLoginChecker() {
        return "member/login-checker";
    }

    @GetMapping("/reset-password")
    public String showEmailForm() {

        return "member/reset-password/email";
    }

    /**
     * 인증된 사용자의 모든 정보를 반환하는 API
     * @param userDetails SecurityContextHolder에 저장된 인증 객체의 principal
     * @return 멤버의 전체 정보
     */
    @GetMapping("/login-checker/me")
    public ResponseEntity<Member> getMemberInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        // @AuthenticationPrincipal에서 직접 사용자 이름을 가져와 서비스 호출
        String username = userDetails.getUsername();
        Member memberInfo = memberService.getMemberByEmail(username);

        return ResponseEntity.ok(memberInfo);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response) {

        try {
            String token = memberService.login(requestDto);

            // JWT를 클라이언트에게 전달하는 가장 표준적인 방법: 응답 헤더에 추가
            // JwtUtil에 상수로 정의해둔 헤더 키와 접두사를 사용
            response.setHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.BEARER_PREFIX + token);

            System.out.println(JwtUtil.AUTHORIZATION_HEADER + " : " + JwtUtil.BEARER_PREFIX + token);
            return ResponseEntity.ok("로그인 성공");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
