package io.cavia.trader.module.post.dto;

import io.cavia.trader.common.validation.ValidPostContent;
import lombok.Data;

@Data
public class PostCreateRequestDto {

    @ValidPostContent
    String content;
}
