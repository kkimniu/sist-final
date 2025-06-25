package io.cavia.trader.module.member.mypage.controller;

import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.mypage.dto.GameParticipationDto;
import io.cavia.trader.module.member.mypage.dto.NicknameUpdateRequestDto;
import io.cavia.trader.module.member.mypage.service.MyPageService;
import io.cavia.trader.module.member.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/mypage")
@RequiredArgsConstructor
public class RestMyPageController {

    private final MyPageService mypageService;
    private final SignupService signupService;

    @GetMapping("/member-id/{memberId}")
    public ResponseEntity<List<GameParticipationDto>> getGameParticipationsByMemberId(@PathVariable int memberId) {
        return ResponseEntity.status(200).body(mypageService.findByMemberId(memberId));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Member> getMembersByEmail(@PathVariable Long id) {
        return ResponseEntity.status(200).body(mypageService.findById(id));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<String> getcheckNickname(@RequestParam String nickname){
        signupService.validateDuplicateNickname(nickname);
        return  ResponseEntity.status(200).body("중복없음");
    }

    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@RequestBody NicknameUpdateRequestDto nicknameUpdateRequestDto) {
        mypageService.changeNickname(nicknameUpdateRequestDto.getId(),nicknameUpdateRequestDto.getNickname(),LocalDateTime.now());
        return  ResponseEntity.status(200).body("변경성공");
    }
}
