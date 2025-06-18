package io.cavia.trader.module.notice.service;

import io.cavia.trader.module.notice.dto.NoticeDto;

import java.util.List;
import java.util.Map;

public interface NoticeService {

    public int saveNotice(NoticeDto dto);

    public boolean existsById(int id);

    public int deleteNotice(int id);

    public int updateNotice(Map map);

    public int patchNotice(Map map);

    public List<NoticeDto> findAll();
}