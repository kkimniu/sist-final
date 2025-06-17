package io.cavia.trader.module.notice.service;

import io.cavia.trader.module.notice.dto.NoticeDto;

public interface NoticeService {

    public int saveNotice(NoticeDto dto);
}