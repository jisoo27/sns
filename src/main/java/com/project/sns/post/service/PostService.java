package com.project.sns.post.service;

import com.project.sns.exception.SnsApplicationException;
import com.project.sns.image.domain.Image;
import com.project.sns.like.domain.Like;
import com.project.sns.like.repository.LikeRepository;
import com.project.sns.post.controller.dto.response.PostEditResponse;
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
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public PostResponse create(List<String> imagePaths, String content, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
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
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(POST_NOT_FOUND));

        if (post.notCheckUser(user)) {
            throw new SnsApplicationException(INVALID_PERMISSION);
        }
        post.edit(content);
        return PostEditResponse.of(post);
    }

    public Page<PostResponse> getMyList(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
        return postRepository.findAllByUser(user, pageable).map(PostResponse::of);
    }

    public void delete(String email, Long postId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(POST_NOT_FOUND));
        if (post.notCheckUser(user)) {
            throw new SnsApplicationException(INVALID_PERMISSION);
        }
        postRepository.delete(post);
    }

    @Transactional
    public void like(String email, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(POST_NOT_FOUND));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));

        likeRepository.findByUserAndPost(user, post).ifPresent(
                it -> {
                    throw new SnsApplicationException(ALREADY_LIKED);
                }
        );
        likeRepository.save(Like.of(user, post));
    }
}
