package io.cavia.trader.module.member.mypage.controller;

import io.cavia.trader.module.member.entity.Member;
import io.cavia.trader.module.member.mypage.dto.GameParticipationDto;
import io.cavia.trader.module.member.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/mypage")
@RequiredArgsConstructor
public class RestMyPageController {

    private final MyPageService mypageService;

    @GetMapping("/member-id/{memberId}")
    public ResponseEntity<List<GameParticipationDto>> getGameParticipationsByMemberId(@PathVariable int memberId){
        return ResponseEntity.status(200).body(mypageService.findByMemberId(memberId));
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<Member> getMembersByEmail(@PathVariable Long id){
        return ResponseEntity.status(200).body(mypageService.findById(id));

    }
}
