package com.project.sns.post.controller.dto.response;

import com.project.sns.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostEditResponse {

    private String content;

    public static PostEditResponse of(Post post) {
        return new PostEditResponse(post.getContent());
    }
}
