package com.project.sns.unit.service.comment;

import com.project.sns.comment.domain.Comment;
import com.project.sns.comment.repository.CommentRepository;
import com.project.sns.comment.service.CommentService;
import com.project.sns.exception.SnsApplicationException;
import com.project.sns.fixture.CommentFixture;
import com.project.sns.fixture.PostFixture;
import com.project.sns.post.domain.Post;
import com.project.sns.post.repository.PostRepository;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.project.sns.exception.ErrorCode.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CommentRepository commentRepository;


    @DisplayName("댓글 생성 요청 시 성공한 경우")
    @Test
    void commentSaveTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();
        Long commentId = 1L;
        Comment comment = CommentFixture.get(email, postId, user.getId(), commentId);
        String firstComment = "첫번째 댓글";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.create(postId, email, firstComment));
    }

    @DisplayName("댓글 생성 요청 시 게시물이 존재하지 않는 경우")
    @Test
    void commentSaveExceptionTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();
        Long commentId = 1L;
        Comment comment = CommentFixture.get(email, postId, user.getId(), commentId);
        String firstComment = "첫번째 댓글";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> commentService.create(postId, email, firstComment));
        assertThat(e.getErrorCode()).isEqualTo(POST_NOT_FOUND);
    }


}
