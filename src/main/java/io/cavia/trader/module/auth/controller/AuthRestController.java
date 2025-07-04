package io.cavia.trader.module.auth.controller;

import io.cavia.trader.common.response.ApiResponse;
import io.cavia.trader.common.response.ApiResponses;
import io.cavia.trader.module.auth.dto.LoginRequestDto;
import io.cavia.trader.module.auth.dto.ResetPasswordRequestDto;
import io.cavia.trader.module.auth.dto.SendCodeRequestDto;
import io.cavia.trader.module.auth.dto.VerifyCodeRequestDto;
import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.auth.service.AuthService;
import io.cavia.trader.module.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequestDto requestDto,
                                                HttpServletResponse response) {
        String token = authService.login(requestDto.getEmail(), requestDto.getPassword());
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.BEARER_PREFIX + token);
        return ApiResponses.ok("토큰이 발급되었습니다", null);
    }

    /**
     * 인증된 사용자의 모든 정보를 반환하는 API
     *
     * @param userDetails SecurityContextHolder에 저장된 인증 객체의 principal
     * @return body: {data: userDetails.getMember()}
     */
    @GetMapping("api/auth/login-checker")
    public ResponseEntity<ApiResponse<?>> getMemberInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponses.ok(userDetails.getMember());
    }

    /**
     * 입력된 이메일로 인증 메일을 발송함
     *
     * @param requestDto 사용자가 입력한 이메일이 들어있는 dto
     * @return 상태코드와 메시지
     */
    @PostMapping("/api/auth/verification/send-code")
    public ResponseEntity<ApiResponse<?>> sendVerificationEmail(@Valid @RequestBody SendCodeRequestDto requestDto) {
        authService.sendVerificationEmail(requestDto.getEmail());
        return ApiResponses.ok();
    }

    /**
     * 인증 코드를 입력받아, 이메일과 인증 코드를 검증하고,
     * 그 이메일의 회원가입 여부에 따라 참 거짓을 응답
     *
     * @param requestDto 사용자가 입력한 이메일과 인증코드가 들어있는 dto
     * @return 상태코드, is-our-member : true/false
     */
    @PostMapping("/api/auth/verification/verify-code")
    public ResponseEntity<ApiResponse<?>> verifyPasswordResetVerification(@Valid @RequestBody VerifyCodeRequestDto requestDto) {
        authService.verifyPasswordResetVerificationRequest(requestDto.getEmail(), requestDto.getAuthKey());
        return ApiResponses.ok();
    }

    /**
     * 완성된 requestDto를 가지고 비밀번호를 재설정함
     *
     * @param requestDto 사용자가 입력한 이메일, 인증코드, 비밀번호가 들어있는 dto
     * @return 응답 body가 없는 204 no content 성공 응답
     */
    @PostMapping("/api/auth/password-reset")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDto requestDto) {
        authService.resetPassword(requestDto.getEmail(), requestDto.getAuthKey(), requestDto.getPassword());
        return ApiResponses.noContent();
    }

}
