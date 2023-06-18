package com.project.sns.unit.service.post;

import com.project.sns.exception.SnsApplicationException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.project.sns.exception.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

    private static final List<ImageRequest> IMAGE_REQUESTS = new ArrayList<>();

    @DisplayName("게시글 등록이 성공한 경우")
    @Test
    void postSaveTest() {

        String email = "admin@email.com";
        String content = "내용이다";
        IMAGE_REQUESTS.add(new ImageRequest("//"));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(imageRepository.save(any())).thenReturn(mock(Image.class));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        assertDoesNotThrow(() -> postService.create(IMAGE_REQUESTS, content, email));
    }

    @DisplayName("게시글 등록한 유저가 없는 경우")
    @Test
    void postSaveExceptionTest() {

        String email = "admin@email.com";
        String content = "내용이다";
        IMAGE_REQUESTS.add(new ImageRequest("//"));

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(imageRepository.save(any())).thenReturn(mock(Image.class));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(IMAGE_REQUESTS, content, email));
        assertThat(e.getErrorCode()).isEqualTo(USER_NOT_FOUND);
    }


}
