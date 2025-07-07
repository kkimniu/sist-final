package io.cavia.trader.module.post.service;

import io.cavia.trader.module.post.entity.Post;

import java.util.List;

public interface PostService {

    List<Post> getPosts(int limit, long offset);

    Post createPost(Post post);
}
