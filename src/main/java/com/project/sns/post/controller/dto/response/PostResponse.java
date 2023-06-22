package com.project.sns.post.controller.dto.response;

import com.project.sns.image.domain.Image;
import com.project.sns.image.dto.ImageResponse;
import com.project.sns.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
public class PostResponse {

    private Long id;

    private String content;

    private List<ImageResponse> imageResponses;

    public static PostResponse of(Post post) {
        List<ImageResponse> imageResponses = new ArrayList<>();
        for (Image image : post.getImages()) {
            imageResponses.add(ImageResponse.of(image));
        }
        return new PostResponse(post.getId(), post.getContent(), imageResponses);
    }
}
