package io.cavia.trader.module.post.service;

import io.cavia.trader.module.post.entity.Post;
import io.cavia.trader.module.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    // 이제 Mapper가 아닌 Repository 인터페이스에 의존합니다.
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Post> getPosts(int limit, long offset) {
        return postRepository.findPosts(limit, offset);
    }

    @Transactional
    @Override
    public Post createPost(Post post) {
        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        postRepository.save(post);
        return post;
    }
}