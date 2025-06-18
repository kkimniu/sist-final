package io.cavia.trader.module.notice.service;

import io.cavia.trader.module.notice.dto.NoticeDto;

import java.util.List;

public interface NoticeService {

    public int saveNotice(NoticeDto dto);
    public boolean existsById(int id);
    public int deleteNotice(int id);
    public int updateNotice(NoticeDto dto);
    public List<NoticeDto> noticeListAll();
}