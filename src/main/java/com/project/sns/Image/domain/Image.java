package com.project.sns.Image.domain;
import com.project.sns.post.domain.Post;
import javax.persistence.*;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Post post;

    private String imagePath;
}
