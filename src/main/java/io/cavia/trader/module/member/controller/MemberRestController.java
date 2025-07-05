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

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getMembersByEmail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponses.ok(userDetails.getMember());
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody PasswordVerificationRequestDto requestDto) {
        memberService.withdrawMember(userDetails.getMember().getId(), requestDto.getCurrentPassword());
        return ApiResponses.noContent();
    }

    @GetMapping("/me/game-participations")
    public ResponseEntity<ApiResponse<?>> getGameParticipations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponses.ok(memberService.getGameParticipationByMemberId(userDetails.getMember().getId()));
    }

    @PostMapping("/me/password/verify")
    public ResponseEntity<ApiResponse<?>> verifyPassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @Valid @RequestBody PasswordVerificationRequestDto requestDto) {
        memberService.validatePassword(userDetails.getMember().getId(), requestDto.getCurrentPassword());
        return ApiResponses.ok();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @Valid @RequestBody PasswordChangeRequestDto requestDto) {
        memberService.processPasswordChangeRequest(userDetails.getMember().getId(),
                requestDto.getCurrentPassword(),
                requestDto.getNewPassword());
        return ApiResponses.noContent();
    }

    @PostMapping("/me/cash/reset")
    public ResponseEntity<Void> resetCash(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.resetCash(userDetails.getMember().getId());
        return ApiResponses.noContent();
    }

    @PatchMapping("/me/nickname")
    public ResponseEntity<Void> updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @Valid @RequestBody NicknameUpdateRequestDto requestDto) {
        memberService.changeNickname(userDetails.getMember().getId(), requestDto.getNickname());
        return ApiResponses.noContent();
    }
}
