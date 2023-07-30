package com.project.sns.comment.service;

import com.project.sns.alarm.domain.Alarm;
import com.project.sns.alarm.domain.AlarmArgs;
import com.project.sns.alarm.domain.AlarmType;
import com.project.sns.alarm.repository.AlarmRepository;
import com.project.sns.comment.domain.Comment;
import com.project.sns.comment.repository.CommentRepository;
import com.project.sns.exception.SnsApplicationException;
import com.project.sns.comment.controller.dto.response.CommentResponse;
import com.project.sns.post.domain.Post;
import com.project.sns.post.repository.PostRepository;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.sns.exception.ErrorCode.*;
import static com.project.sns.exception.ErrorCode.INVALID_PERMISSION;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    public CommentResponse create(Long postId, String email, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(POST_NOT_FOUND));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
        Comment comment = commentRepository.save(Comment.of(user, post, content));
        alarmRepository.save(Alarm.of(post.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(user.getId(), post.getId())));
        return CommentResponse.of(comment, user.getUsername(), postId);
    }

    @Transactional
    public CommentResponse edit(Long postId, Long commentId, String editContent, String email) {
        postRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(POST_NOT_FOUND));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new SnsApplicationException(COMMENT_NOT_FOUND));
        if (comment.isCheckUser(user)) {
            throw new SnsApplicationException(INVALID_PERMISSION);
        }
        comment.edit(editContent);
        return CommentResponse.of(comment, user.getUsername(), postId);
    }

    public void delete(Long postId, Long commentId, String email) {
        postRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(POST_NOT_FOUND));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new SnsApplicationException(COMMENT_NOT_FOUND));
        if (comment.isCheckUser(user)) {
            throw new SnsApplicationException(INVALID_PERMISSION);
        }
        commentRepository.delete(comment);
    }
}
