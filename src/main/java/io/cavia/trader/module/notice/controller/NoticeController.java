package io.cavia.trader.module.notice.controller;

import io.cavia.trader.module.notice.dto.NoticeDto;
import io.cavia.trader.module.notice.exception.InvalidNoticeRequestException;
import io.cavia.trader.module.notice.exception.NotFoundException;
import io.cavia.trader.module.notice.exception.NoticeOperationFailedException;
import io.cavia.trader.module.notice.service.NoticeServiceImple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            throw new InvalidNoticeRequestException("제목은 비워둘 수 없습니다");
        }
        if (noticeDto.getContent() == null || noticeDto.getContent().isBlank()) {
            throw new InvalidNoticeRequestException("내용은 비워둘 수 없습니다");
        }
        noticeDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        int result = noticeServiceImple.saveNotice(noticeDto);
        if (result != 1) {
            throw new NoticeOperationFailedException("공지사항 저장이 실패했습니다");
        }
        return ResponseEntity.status(201).body("공지사항 등록완료");
    }

    @DeleteMapping("deleteNotice")
    public ResponseEntity<String> deleteNotice(@RequestBody NoticeDto noticeDto) {
        if (noticeDto.getId() == 0) {
            throw new InvalidNoticeRequestException("유효하지 않는 ID 입니다");
        }
        boolean exitsts = noticeServiceImple.existsById(noticeDto.getId());
        if (!exitsts) {
            throw new NotFoundException("해당 아이디는 존재하지않습니다.");
        }
        int result = noticeServiceImple.deleteNotice(noticeDto.getId());
        if (result != 1) {
            throw new NoticeOperationFailedException("공지사항 삭제를 실패했습니다.");
        }
        return ResponseEntity.status(201).body("공지사항 삭제완료");
    }

    //@GetMapping("noticListAll")

}
