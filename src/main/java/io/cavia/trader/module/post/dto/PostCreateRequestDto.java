package io.cavia.trader.module.post.dto;

import io.cavia.trader.common.validation.ValidPostContent;

public class PostCreateRequestDto {

    @ValidPostContent
    String content;
}
