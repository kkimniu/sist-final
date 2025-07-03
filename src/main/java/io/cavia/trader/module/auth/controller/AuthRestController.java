package io.cavia.trader.module.auth.controller;

import io.cavia.trader.module.auth.dto.LoginRequestDto;
import io.cavia.trader.module.auth.dto.ResetPasswordRequestDto;
import io.cavia.trader.module.auth.dto.SignupForm;
import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.auth.service.AuthService;
import io.cavia.trader.module.jwt.JwtUtil;
import io.cavia.trader.module.member.entity.Member;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response) {

        try {
            String token = authService.login(requestDto);

            // JWT를 클라이언트에게 전달하는 가장 표준적인 방법: 응답 헤더에 추가
            // JwtUtil에 상수로 정의해둔 헤더 키와 접두사를 사용
            response.setHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.BEARER_PREFIX + token);

            System.out.println(JwtUtil.AUTHORIZATION_HEADER + " : " + JwtUtil.BEARER_PREFIX + token);
            return ResponseEntity.ok("로그인 성공");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * 인증된 사용자의 모든 정보를 반환하는 API
     *
     * @param userDetails SecurityContextHolder에 저장된 인증 객체의 principal
     * @return 멤버의 전체 정보
     */
    @GetMapping("api/auth/login-checker")
    public ResponseEntity<Member> getMemberInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        return ResponseEntity.ok(userDetails.getMember());
    }

    /**
     * 입력된 이메일로 인증 메일을 발송함
     *
     * @param signupForm 사용자가 입력한 이메일이 들어있는 dto
     * @return 상태코드와 메시지
     */
    @PostMapping("/api/auth/verification/send-code")
    public ResponseEntity<?> sendVerificationEmail(@Validated(SignupForm.ValidationGroups.EmailGroup.class) @RequestBody SignupForm signupForm) {
        authService.sendVerificationEmail(signupForm.getEmail());
        return ResponseEntity.ok("이메일 전송 완료");
    }

    /**
     * 인증 코드를 입력받아, 이메일과 인증 코드를 검증하고,
     * 그 이메일의 회원가입 여부에 따라 참 거짓을 응답
     *
     * @param signupForm 사용자가 입력한 이메일과 인증코드가 들어있는 dto
     * @return 상태코드, is-our-member : true/false
     */
    @PostMapping("/api/auth/verification/verify-code")
    public ResponseEntity<?> checkAuthKeyAndIsMember(@Validated(SignupForm.ValidationGroups.EmailVerificationGroup.class) @RequestBody SignupForm signupForm) {
        authService.verifyAuthKey(signupForm.getEmail(), signupForm.getAuthKey());
        if (!authService.isMemberByEmail(signupForm.getEmail())) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("is-our-member", false, "message", "회원가입이 필요한 이메일입니다."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("is-our-member", true, "message", "이메일 인증이 성공했습니다."));
    }

    /**
     * 완성된 requestDto를 가지고 비밀번호를 재설정함
     *
     * @param requestDto 사용자가 입력한 이메일, 인증코드, 비밀번호가 들어있는 dto
     * @return 상태코드와 메시지
     */
    @PostMapping("/api/auth/password-reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto requestDto) {
        authService.resetPassword(requestDto);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }

}
