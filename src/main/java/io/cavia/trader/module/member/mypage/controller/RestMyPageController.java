package io.cavia.trader.module.member.mypage.controller;

import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.mypage.dto.GameParticipationDto;
import io.cavia.trader.module.member.mypage.dto.NicknameUpdateRequestDto;
import io.cavia.trader.module.member.mypage.service.MyPageService;
import io.cavia.trader.module.member.security.UserDetailsImpl;
import io.cavia.trader.module.member.service.MemberService;
import io.cavia.trader.module.notice.exception.InvalidNoticeRequestException;
import io.cavia.trader.module.notice.exception.NotFoundException;
import io.cavia.trader.module.notice.exception.NoticeOperationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/mypage")
@RequiredArgsConstructor
public class RestMyPageController {

    private final MyPageService mypageService;
    private final MemberService memberService;

    @GetMapping("/my-games")
    public ResponseEntity<List<GameParticipationDto>> getGameParticipationsByMemberId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(200).body(mypageService.findByMemberId(userDetails.getMember().getId().intValue()));
    }

    @GetMapping("/me")
    public ResponseEntity<Member> getMembersByEmail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(200).body(mypageService.findById(userDetails.getMember().getId()));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<String> getcheckNickname(@RequestParam String nickname) {
        memberService.validateDuplicateNickname(nickname);
        return ResponseEntity.status(200).body("중복없음");
    }

    @GetMapping("/password-verification")
    public ResponseEntity<String> getcheckPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String password) {
        mypageService.validateDuplicatePassword(userDetails.getMember().getId().intValue(), password);
        return ResponseEntity.status(200).body("비밀번호 맞음");
    }

    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@RequestBody NicknameUpdateRequestDto nicknameUpdateRequestDto) {
        mypageService.changeNickname(nicknameUpdateRequestDto.getId(), nicknameUpdateRequestDto.getNickname(), LocalDateTime.now());
        return ResponseEntity.status(200).body("변경성공");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(mypageService.deletedGameParticipation(userDetails.getMember().getId().intValue())< 0){
            throw new NotFoundException("회원 삭제를 실패했습니다");
        }
        if (mypageService.deleteMember(Integer.parseInt(userDetails.getUsername())) < 0) {
            throw new NoticeOperationFailedException("회원 삭제를 실패했습니다");
        }
        return ResponseEntity.status(200).body("회원 삭제완료");
    }

}
