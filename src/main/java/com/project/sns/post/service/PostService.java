package com.project.sns.post.service;

import com.project.sns.comment.repository.CommentRepository;
import com.project.sns.user.domain.Alarm;
import com.project.sns.user.domain.AlarmArgs;
import com.project.sns.user.domain.AlarmType;
import com.project.sns.user.repository.AlarmRepository;
import com.project.sns.exception.SnsApplicationException;
import com.project.sns.image.domain.Image;
import com.project.sns.like.domain.Like;
import com.project.sns.like.repository.LikeRepository;
import com.project.sns.post.controller.dto.response.PostEditResponse;
import com.project.sns.post.controller.dto.response.PostLikeCountResponse;
import com.project.sns.post.controller.dto.response.PostResponse;
import com.project.sns.post.domain.Post;
import com.project.sns.post.repository.PostRepository;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static com.project.sns.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final AlarmRepository alarmRepository;


    @Transactional
    public PostResponse create(List<String> imagePaths, String content, String email) {
        User user = getUserOrException(email);
        Post post = new Post(content, user);
        for (String imagePath : imagePaths) {
            Image image = Image.of(imagePath, post);
            image.changePost(post);
        }
        postRepository.save(post);
        return PostResponse.of(post);
    }

    @Transactional
    public PostEditResponse edit(String content, String email, Long postId) {
        User user = getUserOrException(email);
        Post post = getPostOrException(postId);

        if (post.notCheckUser(user)) {
            throw new SnsApplicationException(INVALID_PERMISSION);
        }
        post.edit(content);
        return PostEditResponse.of(post);
    }

    public Page<PostResponse> getMyList(String email, Pageable pageable) {
        User user = getUserOrException(email);
        return postRepository.findAllByUser(user, pageable).map(PostResponse::of);
    }

    public void delete(String email, Long postId) {
        User user = getUserOrException(email);
        Post post = getPostOrException(postId);
        if (post.notCheckUser(user)) {
            throw new SnsApplicationException(INVALID_PERMISSION);
        }
        likeRepository.deleteAllByPost(post);
        commentRepository.deleteAllByPost(post);
        postRepository.delete(post);
    }

    @Transactional
    public void like(String email, Long postId) {
        Post post = getPostOrException(postId);
        User user = getUserOrException(email);

        likeRepository.findByUserAndPost(user, post).ifPresent(
                it -> {
                    throw new SnsApplicationException(ALREADY_LIKED);
                }
        );
        likeRepository.save(Like.of(user, post));
        alarmRepository.save(Alarm.of(post.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(user.getId(), post.getId())));
    }

    @Transactional
    public void notLike(String email, Long postId) {
        Post post = getPostOrException(postId);
        User user = getUserOrException(email);
        Like like = likeRepository.findByUserAndPost(user, post).orElseThrow(() -> new SnsApplicationException(LIKE_NOT_FOUND));
        likeRepository.delete(like);
    }

    public Page<PostResponse> getMyLikeList(String email, Pageable pageable) {
        User user = getUserOrException(email);
        return likeRepository.findAllByUser(user, pageable).map(like -> PostResponse.of(like.getPost()));
    }

    public PostLikeCountResponse getLikeCount(Long postId) {
        Post post = getPostOrException(postId);
        long count = likeRepository.countByPost(post);
        return PostLikeCountResponse.builder().count(count).build();
    }

    public Page<PostResponse> getAllList(String email, Pageable pageable) {
        getUserOrException(email);
        return postRepository.findAll(pageable).map(PostResponse::of);
    }

    public Page<PostResponse> getAgeFilterList(String email, int low, int high, Pageable pageable) {
        getUserOrException(email);
        return postRepository.findAllByAge(pageable, low, high).map(PostResponse::of);
    }

    private Post getPostOrException(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new SnsApplicationException(POST_NOT_FOUND)
        );
    }

    private User getUserOrException(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
    }
}
