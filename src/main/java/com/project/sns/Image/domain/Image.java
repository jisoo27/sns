package com.project.sns.image.domain;

import com.project.sns.image.dto.ImageRequest;
import com.project.sns.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String imagePath;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Image(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image(String imagePath, Post post) {
        this.imagePath = imagePath;
        this.post = post;
    }

    public static Image of (String imagePath, Post post) {
        Image image = new Image();
        image.setImagePath(imagePath);
        image.setPost(post);
        return image;
    }

}
