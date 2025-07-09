package io.cavia.trader.module.notice.mapper;

import io.cavia.trader.module.notice.dto.NoticeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 공지사항의 Mapper계층
 * mybaties랑 연결하기 위해서 사용함
 */
@Mapper
public interface NoticeMapper {
    public int saveNotice(NoticeDto dto);
    public int countById(int id);
    public List<NoticeDto> findAll();
    public NoticeDto findById(int id);
    public int deleteNotice(int id);
    public int updateNotice(Map map);
    public int updateOnlyPinned(Map map);
    public int updateTitleAndContent(Map map);
    public List<NoticeDto> findPinned();
    public List<NoticeDto> noticeList(int limit , int offset);
    public int noticeCount();
}