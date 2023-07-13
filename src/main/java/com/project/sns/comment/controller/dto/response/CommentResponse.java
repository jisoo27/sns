package com.project.sns.comment.controller.dto.response;

import com.project.sns.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private String comment;
    private String userName;
    private Long postId;

    public static CommentResponse of (Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUsername(),
                comment.getPost().getId()
        );
    }
}
