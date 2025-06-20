package io.cavia.trader.module.notice.service;

import io.cavia.trader.module.notice.dto.NoticeDto;
import io.cavia.trader.module.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 공지사항 서비스계층
 *
 * @RequiredArgsConstructor는 자동으로 생성자를 만들기 위해서 사용함
 * 생성자주입을 사용한 이유는 @Autowired보다 테스트에 용이하다고 해서 사용함
 * mybatis에서는 서비스를 int로해서 컨트롤러에서 예외처리하는 것이 많다고함
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImple implements NoticeService {
    private final NoticeMapper noticeMapper;

    @Override
    public int saveNotice(NoticeDto dto) {
        int result = noticeMapper.saveNotice(dto);
        return result;
    }

    @Override
    public boolean existsById(int id) {
        int result = noticeMapper.countById(id);
        if (result <= 0) {
            return false;
        }
        return true;
    }


    @Override
    public int deleteNotice(int id) {
        int result = noticeMapper.deleteNotice(id);
        return result;
    }

    @Override
    public int updateNotice(Map map) {
        int result = noticeMapper.updateNotice(map);
        return result;
    }

    @Override
    public int patchNotice(Map map) {
        int result = 0;
        if (map.get("pinned") != null) {
            if (map.get("title") != null || map.get("content") != null) {
                result = noticeMapper.updateNotice(map);
            } else {
                result = noticeMapper.updateOnlyPinned(map);
            }
        } else {
            result = noticeMapper.updateTitleAndContent(map);
        }
        return result;
    }

    @Override
    public List<NoticeDto> findAll() {
        List<NoticeDto> list = noticeMapper.findAll();
        return list;
    }

    @Override
    public NoticeDto findById(int id) {
        NoticeDto dto = noticeMapper.findById(id);
        return dto;
    }

    @Override
    public List<NoticeDto> findPinned() {
        List<NoticeDto> list = noticeMapper.findPinned();
        return list;
    }

    @Override
    public List<NoticeDto> noticeList(int cp, int ls) {
        int offset = (cp - 1) * ls;
        int limit = ls;
        List<NoticeDto> list = noticeMapper.noticeList(limit, offset);
        return list;
    }

    @Override
    public int noticeCount() {
        int result = noticeMapper.noticeCount();
        return result;
    }

}
