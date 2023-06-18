package com.project.sns.post.controller;

import com.project.sns.post.controller.dto.request.PostCreateRequest;
import com.project.sns.post.controller.dto.request.PostEditRequest;
import com.project.sns.post.controller.dto.response.PostEditResponse;
import com.project.sns.post.controller.dto.response.PostResponse;
import com.project.sns.post.service.PostService;
import lombok.RequiredArgsConstructor;
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
        PostResponse response = postService.create(request.getImageRequests(), request.getContent(), authentication.getName());
        return ResponseEntity.created(URI.create("/api/posts/" + response.getId())).body(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostEditResponse> edit(@PathVariable Long postId, @RequestBody PostEditRequest request, Authentication authentication) {
        PostEditResponse response = postService.edit(request.getContent(), authentication.getName(), postId);
        return ResponseEntity.ok().body(response);
    }

}
