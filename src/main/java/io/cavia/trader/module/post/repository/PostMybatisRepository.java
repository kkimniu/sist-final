package io.cavia.trader.module.post.repository;

import io.cavia.trader.module.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostMybatisRepository implements PostRepository {

    private final PostMapper postMapper;

    @Override
    public List<Post> findPosts(int limit, long offset) {
        return postMapper.findPosts(limit, offset);
    }

    @Override
    public void save(Post post) {
        postMapper.save(post);
    }
}
