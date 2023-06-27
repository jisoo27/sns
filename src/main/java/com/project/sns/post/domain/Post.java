package com.project.sns.post.domain;

import com.project.sns.audit.Auditable;
import com.project.sns.image.domain.Image;
import com.project.sns.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "post")
@Getter
@SQLDelete(sql = "UPDATE post SET delete_at = NOW() where id = ?")
@Where(clause = "delete_at is NULL")
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = PROTECTED)
public class Post extends Auditable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    private User user;

    @OneToMany(mappedBy = "post", cascade = ALL)
    private List<Image> images = new ArrayList<>();

    public Post(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public boolean notCheckUser(User user) {
        return this.getUser() != user;
    }

    public void edit(String content) {
        this.content = content;
    }
}
