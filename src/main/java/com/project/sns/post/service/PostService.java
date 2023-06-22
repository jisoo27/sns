package com.project.sns.post.service;

import com.project.sns.exception.SnsApplicationException;
import com.project.sns.image.domain.Image;
import com.project.sns.image.dto.ImageRequest;
import com.project.sns.image.dto.ImageResponse;
import com.project.sns.image.repository.ImageRepository;
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
import java.util.ArrayList;
import java.util.List;

import static com.project.sns.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public PostResponse create(List<ImageRequest> imageRequests, String content, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
        Post post = new Post(content, user);
        for (ImageRequest imageRequest : imageRequests) {
            Image image = new Image(imageRequest.getImagePath(), post);
            post.addImage(image);
            imageRepository.save(image);
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
}
