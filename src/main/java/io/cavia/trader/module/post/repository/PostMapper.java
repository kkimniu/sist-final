package io.cavia.trader.module.post.repository;

import io.cavia.trader.module.post.dto.PostResponseDto;
import io.cavia.trader.module.post.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    /**
     * 무한 스크롤을 위한 게시글 목록 조회
     *
     * @param limit  가져올 게시글 수
     * @param offset 시작 위치
     * @return 게시글 목록
     */
    List<Post> findPosts(@Param("limit") int limit, @Param("offset") long offset);

    Optional<PostResponseDto> findPostResponseDtoById(Long id);

    /**
     * 게시글 저장
     *
     * @param post 저장할 게시글 정보
     */
    void save(Post post);


}