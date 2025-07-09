package io.cavia.trader.module.post.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 닉네임을 포함한 게시글 데이터를 전달하기 위한 dto
 *
 * @author KimBeomhee
 */
@Data
public class PostResponseDto {

    private Long id;

    private String content;

    private Long memberId;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}