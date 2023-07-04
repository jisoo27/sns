package com.project.sns.fixture;

import com.project.sns.like.domain.Like;
import com.project.sns.post.domain.Post;
import com.project.sns.user.domain.User;

public class LikeFixture {

    public static Like get(String email, Long postId, Long userId, Long likeId) {

        User user = User.builder()
                .email(email)
                .id(userId)
                .build();

        Post post = Post.builder()
                .user(user)
                .id(postId)
                .build();

        return Like.builder()
                .post(post)
                .user(user)
                .id(likeId)
                .build();
    }
}
