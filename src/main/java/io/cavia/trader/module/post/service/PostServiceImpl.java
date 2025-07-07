package io.cavia.trader.module.post.service;

import io.cavia.trader.common.exception.ApiException;
import io.cavia.trader.common.exception.ErrorCode;
import io.cavia.trader.module.post.dto.PostCreateRequestDto;
import io.cavia.trader.module.post.dto.PostResponseDto;
import io.cavia.trader.module.post.entity.Post;
import io.cavia.trader.module.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public List<PostResponseDto> getPosts(int limit, long offset) {
        return postRepository.findPosts(limit, offset);
    }

    @Transactional
    @Override
    public PostResponseDto createPost(PostCreateRequestDto requestDto, Long id) {
        Post post = Post.builder()
                .content(requestDto.getContent())
                .memberId(id)
                .build();
        postRepository.save(post);
        return postRepository.findPostResponseDto(post.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.POST_NOT_FOUND));
    }
}