package io.cavia.trader.module.member.controller;

import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.auth.service.AuthService;
import io.cavia.trader.module.member.dto.*;
import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/mypage")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/my-games")
    public ResponseEntity<List<GameParticipationDto>> getGameParticipationsByMemberId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(200).body(memberService.getGameParticipationByMemberId(userDetails.getMember().getId().intValue()));
    }

    @GetMapping("/me")
    public ResponseEntity<Member> getMembersByEmail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = memberService.getMemberById(userDetails.getMember().getId());
        return ResponseEntity.status(200).body(member);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<String> getcheckNickname(@RequestParam String nickname) {
        authService.validateDuplicateNickname(nickname);
        return ResponseEntity.status(200).body("중복없음");
    }

    @PostMapping("/password-verification")
    public ResponseEntity<String> getcheckPassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @Valid @RequestBody PasswordVerificationRequestDto requestDto) {
        memberService.validatePassword(userDetails.getMember().getId(), requestDto.getCurrentPassword());
        return ResponseEntity.status(200).body("비밀번호 일치함");
    }

    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody PasswordChangeRequestDto requestDto) {
        memberService.changePassword(userDetails.getMember().getId(), requestDto);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }

    @PatchMapping("/cash-reset")

    public ResponseEntity<String> resetCash(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.resetCash(userDetails.getMember().getId());
        return ResponseEntity.status(200).body("변경성공");
    }

    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody NicknameUpdateRequestDto nicknameUpdateRequestDto) {
        memberService.changeNickname(userDetails.getMember().getId(), nicknameUpdateRequestDto.getNickname());
        return ResponseEntity.status(200).body("변경성공");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestBody PasswordVerificationRequestDto requestDto) {
        memberService.withdrawMember(userDetails.getMember().getId(), requestDto.getCurrentPassword());
        return ResponseEntity.status(200).body("회원 삭제완료");
    }

    @GetMapping("/rank")
    public ResponseEntity<List<UserRankingDto>> getRanking(@RequestParam String type) {
        if ("cash".equals(type)) {
            return ResponseEntity.status(200).body(memberService.findAllOrderByCash(0, 20));
        } else if ("totalScore".equals(type)) {
            return ResponseEntity.status(200).body(memberService.findAllOrderByTotalScore(0, 20));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
