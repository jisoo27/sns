package com.project.sns.fixture;

import com.project.sns.comment.domain.Comment;
import com.project.sns.post.domain.Post;
import com.project.sns.user.domain.User;

public class CommentFixture {

    public static Comment get(String email, Long postId, Long userId, Long commentId) {

        User user = User.builder()
                .email(email)
                .id(userId)
                .build();

        Post post = Post.builder()
                .user(user)
                .id(postId)
                .build();

        return Comment.builder()
                .post(post)
                .user(user)
                .id(commentId)
                .build();
    }
}
