package io.cavia.trader.module.post.controller; // 본인 프로젝트에 맞는 패키지 경로

import io.cavia.trader.common.response.ApiResponse;
import io.cavia.trader.common.response.ApiResponses;
import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.post.entity.Post;
import io.cavia.trader.module.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    /**
     * 게시글 목록 조회 (무한 스크롤)
     * GET /api/posts?limit=10&offset=0
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getPosts(@RequestParam(defaultValue = "10") int limit,
                                                   @RequestParam(defaultValue = "0") long offset) {
        List<Post> posts = postService.getPosts(limit, offset);
        return ApiResponses.ok(posts);
    }

    /**
     * 게시글 작성
     * POST /api/posts
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody Post post,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        post.setMemberId(userDetails.getMember().getId());
        Post createdPost = postService.createPost(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPost.getId())
                .toUri();
        return ApiResponses.created(location, createdPost);
    }
}
