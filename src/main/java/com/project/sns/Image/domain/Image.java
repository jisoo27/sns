package com.project.sns.image.domain;

import com.project.sns.audit.Auditable;
import com.project.sns.post.domain.Post;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "image")
@Getter
@SQLDelete(sql = "UPDATE image SET delete_at = NOW() where id = ?")
@Where(clause = "delete_at is NULL")
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = PROTECTED)
public class Image extends Auditable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String imagePath;

    @ManyToOne(fetch = LAZY)
    private Post post;

    public Image(String imagePath, Post post) {
        this.imagePath = imagePath;
        this.post = post;
    }

    public static Image of (String imagePath, Post post) {
        return new Image(imagePath, post);
    }

    public void changePost(Post post) {
        if (this.post != null) {
            this.post.getImages().remove(this);
        }
        this.post = post;
        if (!post.getImages().contains(this)) {
            post.getImages().add(this);
        }
    }
}
