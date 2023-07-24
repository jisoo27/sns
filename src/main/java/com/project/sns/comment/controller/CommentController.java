package com.project.sns.comment.controller;

import com.project.sns.comment.service.CommentService;
import com.project.sns.comment.controller.dto.request.PostCommentEditRequest;
import com.project.sns.comment.controller.dto.request.PostCommentRequest;
import com.project.sns.comment.controller.dto.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> create(@PathVariable Long postId, @RequestBody PostCommentRequest request, Authentication authentication) {
        CommentResponse response = commentService.create(postId, authentication.getName(), request.getComment());
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> edit(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody PostCommentEditRequest request, Authentication authentication) {
        CommentResponse response = commentService.edit(postId, commentId, request.getComment(), authentication.getName());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication) {
        commentService.delete(postId, commentId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
