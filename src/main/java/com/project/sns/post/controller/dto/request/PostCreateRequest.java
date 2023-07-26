package com.project.sns.post.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@Getter
public class PostCreateRequest {

    private List<String> imagePaths;

    @NotBlank
    private String content;
}
