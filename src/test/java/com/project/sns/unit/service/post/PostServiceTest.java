package com.project.sns.unit.service.post;

import com.project.sns.exception.SnsApplicationException;
import com.project.sns.fixture.PostFixture;
import com.project.sns.fixture.UserFixture;
import com.project.sns.image.domain.Image;
import com.project.sns.image.dto.ImageRequest;
import com.project.sns.image.repository.ImageRepository;
import com.project.sns.post.domain.Post;
import com.project.sns.post.repository.PostRepository;
import com.project.sns.post.service.PostService;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.project.sns.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ImageRepository imageRepository;

    @DisplayName("게시글 등록이 성공한 경우")
    @Test
    void postSaveTest() {

        String email = "admin@email.com";
        String content = "내용이다";
        List<ImageRequest> imageRequests = new ArrayList<>();
        imageRequests.add(new ImageRequest("//"));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(imageRepository.save(any())).thenReturn(mock(Image.class));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        assertDoesNotThrow(() -> postService.create(imageRequests, content, email));
    }

    @DisplayName("게시글 등록한 유저가 없는 경우")
    @Test
    void postSaveExceptionTest() {

        String email = "admin@email.com";
        String content = "내용이다";
        List<ImageRequest> imageRequests = new ArrayList<>();
        imageRequests.add(new ImageRequest("//"));

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(imageRepository.save(any())).thenReturn(mock(Image.class));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.create(imageRequests, content, email));
        assertThat(e.getErrorCode()).isEqualTo(USER_NOT_FOUND);
    }

    @DisplayName("게시글 수정이 성공한 경우")
    @Test
    void postEditTest() {

        String email = "admin@email.com";
        String content = "수정된 내용이다.";
        List<ImageRequest> imageRequests = new ArrayList<>();
        imageRequests.add(new ImageRequest("//"));
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.saveAndFlush(any())).thenReturn(post);

        assertDoesNotThrow(() -> postService.edit(content, email, postId));
    }

    @DisplayName("게시글 수정 시 게시글이 존재하지 않는 경우")
    @Test
    void postEditExceptionTest() {

        String email = "admin@email.com";
        String content = "수정된 내용이다.";
        List<ImageRequest> imageRequests = new ArrayList<>();
        imageRequests.add(new ImageRequest("//"));
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.edit(content, email, postId));
        assertThat(e.getErrorCode()).isEqualTo(POST_NOT_FOUND);
    }

    @DisplayName("게시글 수정시 권한이 없는 경우")
    @Test
    void postEditExceptionTest2() {

        String email = "admin@email.com";
        String content = "수정된 내용이다.";
        List<ImageRequest> imageRequests = new ArrayList<>();
        imageRequests.add(new ImageRequest("//"));
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = UserFixture.get(email, "password", 2L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.edit(content, email, postId));
        assertThat(e.getErrorCode()).isEqualTo(INVALID_PERMISSION);
    }


    @DisplayName("자신이 쓴 게시물 조회에 성공할 경우")
    @Test
    void getMyPostListTest() {

        Pageable pageable = mock(Pageable.class);
        User user = mock(User.class);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(postRepository.findAllByUser(user, pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> postService.getMyList(user.getEmail(), pageable));
    }

}
