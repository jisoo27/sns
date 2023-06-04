package com.project.sns.user.domain;

import com.project.sns.audit.Auditable;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Builder
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
    private final UserRole role = UserRole.USER;

//    @ManyToOne
//    private Post post;
//
//    @ManyToOne
//    private Like like;

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

}
