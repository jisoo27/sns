package com.project.sns.post.controller.dto.request;

import com.project.sns.image.dto.ImageRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostCreateRequest {

    private List<ImageRequest> imageRequests;

    private String content;
}
