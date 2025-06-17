package io.cavia.trader.module.notice.mapper;

import io.cavia.trader.module.notice.dto.NoticeDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 공지사항의 Mapper계층
 * mybaties랑 연결하기 위해서 사용함
 */
@Mapper
public interface NoticeMapper {
    public int saveNotice(NoticeDto dto);
}
