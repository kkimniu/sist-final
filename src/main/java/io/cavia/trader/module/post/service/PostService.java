package io.cavia.trader.module.post.service;

import io.cavia.trader.module.post.dto.PostCreateRequestDto;
import io.cavia.trader.module.post.dto.PostResponseDto;
import io.cavia.trader.module.post.entity.Post;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostService {

    List<PostResponseDto> getPosts(int limit, long offset);

    PostResponseDto createPost(PostCreateRequestDto requestDto, Long id);
}
