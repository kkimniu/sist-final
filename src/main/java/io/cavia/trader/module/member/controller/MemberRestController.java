package io.cavia.trader.module.member.controller;

import io.cavia.trader.common.response.ApiResponse;
import io.cavia.trader.common.response.ApiResponses;
import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.member.dto.NicknameUpdateRequestDto;
import io.cavia.trader.module.member.dto.PasswordChangeRequestDto;
import io.cavia.trader.module.member.dto.PasswordVerificationRequestDto;
import io.cavia.trader.module.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;

    /**
     * 현재 로그인된 사용자의 상세 정보를 조회합니다.
     *
     * @param userDetails SecurityContextHolder에 저장된 사용자 정보
     * @return Member 엔티티를 ApiResponse.data에 담은 200 OK 응답
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getMembersByEmail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponses.ok(userDetails.getMember());
    }

    /**
     * 현재 로그인된 사용자의 회원 탈퇴를 처리합니다.
     *
     * @param userDetails SecurityContextHolder에 저장된 사용자 정보
     * @param requestDto  현재 비밀번호 확인을 위한 DTO
     * @return 본문 없는 204 No Content 응답
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody PasswordVerificationRequestDto requestDto) {
        memberService.withdrawMember(userDetails.getMember().getId(), requestDto.getCurrentPassword());
        return ApiResponses.noContent();
    }

    /**
     * 현재 로그인된 사용자의 모든 게임 참여 기록을 조회합니다.
     *
     * @param userDetails SecurityContextHolder에 저장된 사용자 정보
     * @return 게임 참여 기록 리스트를 ApiResponse.data에 담은 200 OK 응답
     */
    @GetMapping("/me/game-participations")
    public ResponseEntity<ApiResponse<?>> getGameParticipations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponses.ok(memberService.getGameParticipationByMemberId(userDetails.getMember().getId()));
    }

    /**
     * 현재 로그인된 사용자의 비밀번호가 일치하는지 검증합니다.
     *
     * @param userDetails SecurityContextHolder에 저장된 사용자 정보
     * @param requestDto  검증할 현재 비밀번호를 담은 DTO
     * @return OK 메시지를 담은 200 OK 응답
     */
    @PostMapping("/me/password/verify")
    public ResponseEntity<ApiResponse<?>> verifyPassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @Valid @RequestBody PasswordVerificationRequestDto requestDto) {
        memberService.validatePassword(userDetails.getMember().getId(), requestDto.getCurrentPassword());
        return ApiResponses.ok();
    }

    /**
     * 현재 로그인된 사용자의 비밀번호를 입력받아 새로운 비밀번호로 변경합니다.
     *
     * @param userDetails SecurityContextHolder에 저장된 사용자 정보
     * @param requestDto  현재 비밀번호와 새 비밀번호를 담은 DTO
     * @return 본문 없는 204 No Content 응답
     */
    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @Valid @RequestBody PasswordChangeRequestDto requestDto) {
        memberService.processPasswordChangeRequest(userDetails.getMember().getId(),
                requestDto.getCurrentPassword(),
                requestDto.getNewPassword());
        return ApiResponses.noContent();
    }

    /**
     * 현재 로그인된 사용자의 보유 자산을 초기화합니다.
     *
     * @param userDetails SecurityContextHolder에 저장된 사용자 정보
     * @return 본문 없는 204 No Content 응답
     */
    @PostMapping("/me/cash/reset")
    public ResponseEntity<Void> resetCash(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.resetCash(userDetails.getMember().getId());
        return ApiResponses.noContent();
    }

    /**
     * 현재 로그인된 사용자의 닉네임을 변경합니다.
     *
     * @param userDetails SecurityContextHolder에 저장된 사용자 정보
     * @param requestDto  변경할 새 닉네임을 담은 DTO
     * @return 본문 없는 204 No Content 응답
     */
    @PatchMapping("/me/nickname")
    public ResponseEntity<Void> updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @Valid @RequestBody NicknameUpdateRequestDto requestDto) {
        memberService.changeNickname(userDetails.getMember().getId(), requestDto.getNickname());
        return ApiResponses.noContent();
    }
}
