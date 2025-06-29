package io.cavia.trader.module.member.controller;

import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.dto.GameParticipationDto;
import io.cavia.trader.module.member.dto.NicknameUpdateRequestDto;
import io.cavia.trader.module.member.dto.PasswordRequestDto;
import io.cavia.trader.module.member.service.MemberService;
import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.auth.service.AuthService;
import io.cavia.trader.module.notice.exception.NoticeOperationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/mypage")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService mypageService;
    private final AuthService authService;

    @GetMapping("/my-games")
    public ResponseEntity<List<GameParticipationDto>> getGameParticipationsByMemberId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(200).body(mypageService.getGameParticipationByMemberId(userDetails.getMember().getId().intValue()));
    }

    @GetMapping("/me")
    public ResponseEntity<Member> getMembersByEmail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(200).body(mypageService.getMemberById(userDetails.getMember().getId()));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<String> getcheckNickname(@RequestParam String nickname) {
        authService.validateDuplicateNickname(nickname);
        return ResponseEntity.status(200).body("중복없음");
    }

    @PostMapping("/password-verification")
    public ResponseEntity<String> getcheckPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto passwordRequestDto) {
        if (!mypageService.validatePassword(userDetails.getMember().getId(), passwordRequestDto.getPassword())) {
            return ResponseEntity.status(400).body("비밀번호가 일치하지 않습니다.");
        }
        return ResponseEntity.status(200).body("비밀번호 맞음");
    }

    @PatchMapping("/cash-reset")
    public ResponseEntity<String> resetCash(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        mypageService.resetCash(userDetails.getMember().getId());
        return ResponseEntity.status(200).body("변경성공");
    }

    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody NicknameUpdateRequestDto nicknameUpdateRequestDto) {
        mypageService.changeNickname(userDetails.getMember().getId(), nicknameUpdateRequestDto.getNickname());
        return ResponseEntity.status(200).body("변경성공");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto passwordRequestDto) {
        if (!mypageService.validatePassword(userDetails.getMember().getId(), passwordRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (mypageService.deleteMember(userDetails.getMember().getId(), passwordRequestDto.getPassword()) <= 0) {
            throw new NoticeOperationFailedException("회원 삭제를 실패했습니다");
        }
        return ResponseEntity.status(200).body("회원 삭제완료");
    }

}
