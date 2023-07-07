package com.project.sns.like.domain;

import com.project.sns.audit.Auditable;
import com.project.sns.post.domain.Post;
import com.project.sns.user.domain.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "likes")
@Getter
@SQLDelete(sql = "UPDATE likes SET delete_at = NOW() where id = ?")
@Where(clause = "delete_at is NULL")
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = PROTECTED)
public class Like extends Auditable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    private Post post;

    public static Like of(User user, Post post) {
        return Like.builder()
                .user(user)
                .post(post)
                .build();
    }

}
