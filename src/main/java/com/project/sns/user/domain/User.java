package com.project.sns.user.domain;

import com.project.sns.audit.Auditable;
import com.project.sns.user.controller.dto.request.UserJoinRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "users")
@Getter
@SQLDelete(sql = "UPDATE users SET delete_at = NOW() where id = ?")
@Where(clause = "delete_at is NULL")
@NoArgsConstructor(access = PROTECTED)
public class User extends Auditable {

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
    private UserRole role = UserRole.USER;

//    @ManyToOne
//    private Post post;
//
//    @ManyToOne
//    private Like like;

    @Builder
    public User(int age, String email, String password, String userName, String nickName, String address, String profileMessage, String profileImage) {
        this.age = age;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.nickName = nickName;
        this.address = address;
        this.profileMessage = profileMessage;
        this.profileImage = profileImage;
    }

    public static User of (UserJoinRequest userJoinRequest) {
        return User.builder()
                .age(userJoinRequest.getAge())
                .email(userJoinRequest.getEmail())
                .password(userJoinRequest.getPassword())
                .userName(userJoinRequest.getUserName())
                .nickName(userJoinRequest.getNickName())
                .address(userJoinRequest.getAddress())
                .profileMessage(userJoinRequest.getProfileMessage())
                .profileImage(userJoinRequest.getProfileImage())
                .build();
    }

}
