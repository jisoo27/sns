package com.project.sns.comment.domain;

import com.project.sns.audit.Auditable;
import com.project.sns.post.domain.Post;
import com.project.sns.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment", indexes = {
        @Index(name = "post_Id_idx", columnList = "post_id")
})
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    private Post post;

    private String content;

    public boolean isCheckUser(User user) {
        return this.getUser() != user;
    }

    public static Comment of(User user, Post post, String content) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
    }

    public void edit(String content) {
        this.content = content;
    }
}
