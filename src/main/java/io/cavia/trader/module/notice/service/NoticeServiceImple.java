package io.cavia.trader.module.notice.service;

import io.cavia.trader.module.notice.dto.NoticeDto;
import io.cavia.trader.module.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
