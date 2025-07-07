package io.cavia.trader.module.post.controller;

import io.cavia.trader.common.response.ApiResponse;
import io.cavia.trader.common.response.ApiResponses;
import io.cavia.trader.module.auth.security.UserDetailsImpl;
import io.cavia.trader.module.post.dto.PostCreateRequestDto;
import io.cavia.trader.module.post.dto.PostResponseDto;
import io.cavia.trader.module.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * 게시글(Post) 관련 REST API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    /**
     * 게시글 목록을 무한 스크롤 방식으로 조회합니다.
     * HTTP GET 요청을 '/api/posts' 경로로 받습니다.
     *
     * @param limit  한 번에 조회할 게시글의 수. 기본값은 10입니다. (예: /api/posts?limit=20)
     * @param offset 조회를 시작할 위치(id). 이 값부터 limit 개수만큼 조회합니다. 기본값은 0입니다. (예: /api/posts?offset=10)
     * @return 성공 시, ApiResponse.data에 List<PostResponseDto> 가 담긴 200 OK 응답
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getPosts(@RequestParam(defaultValue = "10") int limit,
                                                   @RequestParam(defaultValue = "0") long offset) {
        List<PostResponseDto> posts = postService.getPosts(limit, offset);
        return ApiResponses.ok(posts);
    }

    /**
     * 새로운 게시글을 작성합니다.
     *
     * @param requestDto  요청 본문(JSON)에서 받은 게시글 내용({ "content": "..." })을 담는 DTO.
     * @param userDetails Spring Security 컨텍스트에서 현재 인증된(로그인한) 사용자의 정보를 가져옵니다.
     * @return 성공 시, 생성된 리소스의 위치(URI)를 헤더에 포함하고,
     * 생성된 게시글의 정보(PostResponseDto)를 본문에 담아 201 Created 응답
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody PostCreateRequestDto requestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto createdPost = postService.createPost(requestDto, userDetails.getMember().getId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPost.getId())
                .toUri();
        return ApiResponses.created(location, createdPost);
    }
}