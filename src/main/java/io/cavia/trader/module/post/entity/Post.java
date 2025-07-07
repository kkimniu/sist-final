package io.cavia.trader.module.post.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 게시글 데이터를 담는 클래스
 *
 * @author KimBeomhee
 */
@Data
public class Post {

    private Long id;

    private String content; // 내용

    private Long memberId; // 작성자 ID (FK)

    private LocalDateTime createdAt; // 작성일

    private LocalDateTime updatedAt; // 수정일
}