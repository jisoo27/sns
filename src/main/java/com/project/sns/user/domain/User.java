package com.project.sns.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.sns.audit.Auditable;
import com.project.sns.comment.domain.Comment;
import com.project.sns.like.domain.Like;
import com.project.sns.post.domain.Post;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "users")
@ToString
@Getter
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private int age;

    private String email;

    private String password;

    private String userName;

    private String nickName;

    private String address;

    private String profileMessage;

    private String profileImage;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private final UserRole role = UserRole.USER;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Like> likes = new ArrayList<>();


    public void editInfo(String profileMessage, String profileImage, String nickName) {
        this.profileMessage = profileMessage;
        this.profileImage = profileImage;
        this.nickName = nickName;
    }


    public static User of (int age, String email, String password, String userName, String nickName, String address, String profileMessage, String profileImage) {
        return User.builder()
                .age(age)
                .email(email)
                .password(password)
                .userName(userName)
                .nickName(nickName)
                .address(address)
                .profileMessage(profileMessage)
                .profileImage(profileImage)
                .build();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().toString()));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return this.getDeletedAt() == null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return this.getDeletedAt() == null;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return this.getDeletedAt() == null;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.getDeletedAt() == null;
    }
}
