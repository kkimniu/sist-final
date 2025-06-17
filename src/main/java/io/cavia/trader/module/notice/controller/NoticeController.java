package io.cavia.trader.module.notice.controller;

import io.cavia.trader.module.notice.dto.NoticeDto;
import io.cavia.trader.module.notice.exception.NoticeSaveFailedException;
import io.cavia.trader.module.notice.service.NoticeServiceImple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

/**
 * 공지사항 저장하는 컨트롤러 만듬 예외처리는 컨트롤러에서햇음
 * 이유는 이전에 선생님도 컨트롤러에서 예외처리했어서 따라함
 * noticeDto.setCreate_at(new Timestamp(System.currentTimeMillis()));
 * 이유는 컴럼값이 Timestamp로 되어잇어서 거기에 맞추고 싶어서 사용함
 * 따로 java에서 자장한 이유는 컬럼에 자동으로 저장하지않기 때문에
 */
@RestController
@RequestMapping("notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeServiceImple noticeServiceImple;

    @PostMapping("saveNotice")
    public ResponseEntity<String> saveNotice(@RequestBody NoticeDto noticeDto) {
        if (noticeDto.getTitle() == null || noticeDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목은 비워둘 수 없습니다");
        }
        if (noticeDto.getContent() == null || noticeDto.getContent().isBlank()) {
            throw new IllegalArgumentException("내용은 비워둘 수 없습니다");
        }
        noticeDto.setCreatedat(new Timestamp(System.currentTimeMillis()));
        int result = noticeServiceImple.saveNotice(noticeDto);
        if (result != 1) {
            throw new NoticeSaveFailedException("공지사항 저장이 실패했습니다");
        }
        return ResponseEntity.status(201).body("공지사항 등록완료");
    }
}
