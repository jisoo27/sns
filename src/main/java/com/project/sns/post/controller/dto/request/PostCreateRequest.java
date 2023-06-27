package com.project.sns.post.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostCreateRequest {

    private List<String> imagePaths;

    private String content;
}
