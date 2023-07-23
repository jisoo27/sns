package com.project.sns.fixture;

import com.project.sns.post.domain.Post;
import com.project.sns.user.domain.User;

public class PostFixture {

    public static Post get(String email, Long postId, Long userId) {
        User user = User.builder()
                .email(email)
                .userName("user")
                .id(userId)
                .build();
        return Post.builder()
                .user(user)
                .id(postId)
                .build();
    }
}
