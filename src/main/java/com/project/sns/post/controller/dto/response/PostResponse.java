package com.project.sns.post.controller.dto.response;

import com.project.sns.image.dto.ImageResponse;
import com.project.sns.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@AllArgsConstructor
public class PostResponse {

    private Long id;

    private String content;

    private List<ImageResponse> imageList;

    public static PostResponse of(Post post) {
        List<ImageResponse> imageResponses = post.getImages().stream().map(ImageResponse::of).collect(Collectors.toList());
        return new PostResponse(post.getId(), post.getContent(), imageResponses);
    }
}
