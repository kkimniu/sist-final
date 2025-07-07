package io.cavia.trader.module.post.dto;

import io.cavia.trader.common.validation.ValidPostContent;
import lombok.Data;

/**
 * 게시글을 등록하기 위한 요청 데이터를 담는 dto
 *
 * @author KimBeomhee
 */
@Data
public class PostCreateRequestDto {

    @ValidPostContent
    String content;
}
