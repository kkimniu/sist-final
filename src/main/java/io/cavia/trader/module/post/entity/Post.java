package io.cavia.trader.module.post.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 게시글 데이터를 담는 도메인 객체
 *
 * @author KimBeomhee
 */
@Data
@Builder
public class Post {

    private Long id;

    private String content;

    private Long memberId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}