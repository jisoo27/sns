package com.project.sns.post.controller;

import com.project.sns.post.controller.dto.request.PostCreateRequest;
import com.project.sns.post.controller.dto.response.PostResponse;
import com.project.sns.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        PostResponse postResponse = postService.create(request.getImageRequests(), request.getContent(), authentication.getName());
        return ResponseEntity.ok().body(postResponse);
    }

}
