package io.cavia.trader.module.notice.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @NoArgsConstructor
 * @AllArgsConstructor
 * @Getter
 * @Setter
 * @ToString
 * 를 대처하는 @Data를 사용함
 */
@Data
public class NoticeDto {
    private int id;
    private String title;
    private String content;
    private Timestamp created_at;
    private Timestamp updated_at;
    private boolean pinned;
}
