package io.cavia.trader.module.post.repository;

import io.cavia.trader.module.post.entity.Post;

import java.util.List;

public interface PostRepository {
    /**
     * 무한 스크롤을 위한 게시글 목록 조회
     *
     * @param limit  가져올 게시글 수
     * @param offset 시작 위치
     * @return 게시글 목록
     */
    List<Post> findPosts(int limit, long offset);

    /**
     * 게시글 저장
     *
     * @param post 저장할 게시글 정보
     */
    void save(Post post);
}
