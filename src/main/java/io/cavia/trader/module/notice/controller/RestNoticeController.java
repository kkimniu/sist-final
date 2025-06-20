package io.cavia.trader.module.notice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cavia.trader.module.notice.dto.NoticeDto;
import io.cavia.trader.module.notice.exception.InvalidNoticeRequestException;
import io.cavia.trader.module.notice.exception.NotFoundException;
import io.cavia.trader.module.notice.exception.NoticeOperationFailedException;
import io.cavia.trader.module.notice.service.NoticeService;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 공지사항 저장하는 컨트롤러 만듬 예외처리는 컨트롤러에서햇음
 * 이유는 이전에 선생님도 컨트롤러에서 예외처리했어서 따라함
 * noticeDto.setCreate_at(new Timestamp(System.currentTimeMillis()));
 * 이유는 컴럼값이 Timestamp로 되어잇어서 거기에 맞추고 싶어서 사용함
 * 따로 java에서 자장한 이유는 컬럼에 자동으로 저장하지않기 때문에
 */
@RestController
@RequestMapping("api/notices")
@RequiredArgsConstructor
public class RestNoticeController {

    private final NoticeService noticeService;
    private final ObjectMapper mapper;

    @PostMapping
    public ResponseEntity<String> saveNotice(@RequestBody NoticeDto noticeDto) {
        if (noticeDto.getTitle() == null || noticeDto.getTitle().isBlank()) {
            throw new InvalidNoticeRequestException("제목은 비워둘 수 없습니다");
        }
        if (noticeDto.getContent() == null || noticeDto.getContent().isBlank()) {
            throw new InvalidNoticeRequestException("내용은 비워둘 수 없습니다");
        }
        noticeDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        int result = noticeService.saveNotice(noticeDto);
        if (result != 1) {
            throw new NoticeOperationFailedException("공지사항 저장이 실패했습니다");
        }
        return ResponseEntity.status(201).body("공지사항 등록완료");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotice(@PathVariable int id) {
        if (id == 0) {
            throw new InvalidNoticeRequestException("유효하지 않는 ID 입니다");
        }
        boolean exists = noticeService.existsById(id);
        if (!exists) {
            throw new NotFoundException("해당 아이디는 존재하지않습니다.");
        }
        int result = noticeService.deleteNotice(id);
        if (result != 1) {
            throw new NoticeOperationFailedException("공지사항 삭제를 실패했습니다");
        }
        return ResponseEntity.status(200).body("공지사항 삭제완료");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateNotice(@PathVariable int id, ServletRequest request) throws Exception {
        Map map = new HashMap<>();
        if (id == 0) {
            throw new InvalidNoticeRequestException("유효하지 않는 ID 입니다");
        }
        boolean exists = noticeService.existsById(id);
        if (!exists) {
            throw new NotFoundException("해당 아이디는 존재하지않습니다");
        }
        map.put("id", id);
        //원문 추출
        String json = request.getReader().lines().collect(Collectors.joining());

        //파싱
        JsonNode root = mapper.readTree(json);

        //매핑
        NoticeDto noticeDto = mapper.readValue(json, NoticeDto.class);
        //사용자가 보낸 필드 확인
        boolean booleanTitle = root.has("title") && !root.get("title").asText().isBlank();
        boolean booleanContent = root.has("content") && !root.get("content").asText().isBlank();
        boolean booleanPinned = root.has("pinned");

        if (!booleanTitle && !booleanContent && !booleanPinned) {
            throw new InvalidNoticeRequestException("변경할 필드를 입력해주세요");
        }
        if (!booleanTitle) {
            throw new InvalidNoticeRequestException("title을 입력해주세요");
        }
        if (!booleanContent) {
            throw new InvalidNoticeRequestException("content을 입력해주세요");
        }
        if (!booleanPinned) {
            throw new InvalidNoticeRequestException("pinned을 입력해주세요");
        }

        map.put("title", noticeDto.getTitle());
        map.put("content", noticeDto.getContent());
        map.put("pinned", noticeDto.isPinned());
        map.put("updatedAt", new Timestamp(System.currentTimeMillis()));
        int result = noticeService.updateNotice(map);
        if (result != 1) {
            throw new NoticeOperationFailedException("공지사항 수정을 실패했습니다");
        }
        return ResponseEntity.status(200).body("전체 공지사항 수정 완료");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchNotice(@PathVariable int id, ServletRequest request) throws Exception {
        if (id == 0) {
            throw new InvalidNoticeRequestException("유효하지 않는 ID 입니다");
        }
        boolean exists = noticeService.existsById(id);
        if (!exists) {
            throw new NotFoundException("해당 아이디는 존재하지않습니다");
        }

        Map map = new HashMap<>();
        //원문 추출
        String json = request.getReader().lines().collect(Collectors.joining());

        //파싱
        JsonNode root = mapper.readTree(json);

        //매핑
        NoticeDto noticeDto = mapper.readValue(json, NoticeDto.class);

        map.put("id", id);
        //사용자가 보낸 필드 확인
        boolean booleanTitle = root.has("title") && !root.get("title").asText().isBlank();
        boolean booleanContent = root.has("content") && !root.get("content").asText().isBlank();
        boolean booleanPinned = root.has("pinned");

        if (!booleanTitle && !booleanContent && !booleanPinned) {
            throw new InvalidNoticeRequestException("변경할 필드를 입력해주세요");
        }
        if (booleanTitle) {
            map.put("title", noticeDto.getTitle());
        }
        if (booleanContent) {
            map.put("content", noticeDto.getContent());
        }
        if (booleanPinned) {
            map.put("pinned", noticeDto.isPinned());
        }
        map.put("updatedAt", new Timestamp(System.currentTimeMillis()));
        int result = noticeService.patchNotice(map);
        if (result != 1) {
            throw new NoticeOperationFailedException("공지사항 수정을 실패했습니다");
        }
        return ResponseEntity.status(200).body("공지사항 부분 수정 완료");
    }

    @GetMapping
    public ResponseEntity<List<NoticeDto>> getAllNotices() {
        return ResponseEntity.status(200).body(noticeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto> getNotices(@PathVariable int id) {
        return ResponseEntity.status(200).body(noticeService.findById(id));
    }
}
