package com.project.sns.post.controller;

import com.project.sns.post.controller.dto.request.PostCreateRequest;
import com.project.sns.post.controller.dto.request.PostEditRequest;
import com.project.sns.post.controller.dto.response.PostLikeCountResponse;
import com.project.sns.post.controller.dto.response.PostEditResponse;
import com.project.sns.post.controller.dto.response.PostResponse;
import com.project.sns.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        PostResponse response = postService.create(request.getImagePaths(), request.getContent(), authentication.getName());
        return ResponseEntity.created(URI.create("/api/posts/" + response.getId())).body(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostEditResponse> edit(@PathVariable Long postId, @RequestBody PostEditRequest request, Authentication authentication) {
        PostEditResponse response = postService.edit(request.getContent(), authentication.getName(), postId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<PostResponse>> getMyList(Pageable pageable, Authentication authentication) {
        Page<PostResponse> myList = postService.getMyList(authentication.getName(), pageable);
        return ResponseEntity.ok().body(myList);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getList(Pageable pageable, Authentication authentication) {
        Page<PostResponse> list = postService.getList(authentication.getName(), pageable);
        return ResponseEntity.ok().body(list);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> like(@PathVariable Long postId, Authentication authentication) {
        postService.like(authentication.getName(), postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<Void> notLike(@PathVariable Long postId, Authentication authentication) {
        postService.notLike(authentication.getName(), postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/likes")
    public ResponseEntity<Page<PostResponse>> getMyLikeList(Pageable pageable, Authentication authentication) {
        Page<PostResponse> list = postService.getMyLikeList(authentication.getName(), pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<PostLikeCountResponse> getLikeCount(@PathVariable Long postId, Authentication authentication) {
        PostLikeCountResponse response = postService.getLikeCount(postId);
        return ResponseEntity.ok().body(response);
    }
}
