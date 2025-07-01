package io.cavia.trader.module.member.controller;

import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.member.dto.GameParticipationDto;
import io.cavia.trader.module.member.dto.NicknameUpdateRequestDto;
import io.cavia.trader.module.member.dto.PasswordChangeRequestDto;
import io.cavia.trader.module.member.dto.PasswordVerificationRequestDto;
import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<Member> getMembersByEmail(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.status(200)
                .body(memberService.getMemberById(userDetails.getMember().getId()));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestBody PasswordVerificationRequestDto requestDto) {
        memberService.withdrawMember(userDetails.getMember().getId(), requestDto.getCurrentPassword());
        return ResponseEntity.status(200).body("회원 삭제완료");
    }

    @GetMapping("/me/game-participations")
    public ResponseEntity<List<GameParticipationDto>> getGameParticipationsByMemberId(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(200)
                .body(memberService.getGameParticipationByMemberId(userDetails.getMember().getId()));
    }

    @PostMapping("/me/password/verify")
    public ResponseEntity<String> getcheckPassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @Valid @RequestBody PasswordVerificationRequestDto requestDto) {
        memberService.validatePassword(userDetails.getMember().getId(), requestDto.getCurrentPassword());
        return ResponseEntity.status(200).body("비밀번호 일치함");
    }

    @PatchMapping("/me/password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody PasswordChangeRequestDto requestDto) {
        memberService.changePassword(userDetails.getMember().getId(), requestDto);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }

    @PostMapping("/me/cash/reset")
    public ResponseEntity<String> resetCash(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.resetCash(userDetails.getMember().getId());
        return ResponseEntity.status(200).body("변경성공");
    }

    @PatchMapping("/me/nickname")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @RequestBody NicknameUpdateRequestDto requestDto) {
        memberService.changeNickname(userDetails.getMember().getId(), requestDto.getNickname());
        return ResponseEntity.status(200).body("변경성공");
    }
}
