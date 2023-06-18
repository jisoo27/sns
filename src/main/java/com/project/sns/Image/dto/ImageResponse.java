package com.project.sns.image.dto;

import com.project.sns.image.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageResponse {

    private String imagePath;

    public static ImageResponse of (Image image) {
        return new ImageResponse(image.getImagePath());
    }
}
