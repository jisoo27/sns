package com.project.sns.post.controller.dto.response;

import com.project.sns.image.dto.ImageRequest;
import com.project.sns.image.dto.ImageResponse;
import com.project.sns.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class PostResponse {

    private String content;

    private List<ImageResponse> imageResponses;

    public static PostResponse of(Post post, List<ImageResponse> imageRequests) {
        return new PostResponse(post.getContent(), imageRequests);
    }
}
