package com.project.sns.unit.service.comment;

import com.project.sns.comment.domain.Comment;
import com.project.sns.comment.repository.CommentRepository;
import com.project.sns.comment.service.CommentService;
import com.project.sns.exception.SnsApplicationException;
import com.project.sns.fixture.CommentFixture;
import com.project.sns.fixture.PostFixture;
import com.project.sns.fixture.UserFixture;
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
import static com.project.sns.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;


    @DisplayName("댓글 생성 요청 시 성공한 경우")
    @Test
    void commentSaveTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);

        String firstComment = "첫번째 댓글";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(mock(Post.class)));
        when(commentRepository.save(any())).thenReturn(mock(Comment.class));

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

    @DisplayName("댓글 수정 요청 시 성공한 경우")
    @Test
    void editCommentTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";
        Long commentId = 1L;
        Comment comment = CommentFixture.get(anotherEmail, postId, 2L, commentId);
        User anotherUser = comment.getUser();
        String editComment = "수정된 댓글";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.edit(postId, commentId, editComment, anotherEmail));
    }

    @DisplayName("댓글 수정 요청시 댓글이 존재하지 않는 경우")
    @Test
    void editCommentExceptionTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        Long commentId = 1L;

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> commentService.edit(postId, commentId, "수정 댓글",email));
        assertThat(e.getErrorCode()).isEqualTo(COMMENT_NOT_FOUND);
    }

    @DisplayName("댓글 수정시 본인이 작성한 댓글이 아닐 경우")
    @Test
    void editCommentExceptionTest2() {

        String email = "admin@email.com";
        Long postId = 1L;
        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";
        User anotherUser = UserFixture.get(anotherEmail, "kkk", 2L);

        Long commentId = 1L;
        Comment comment = CommentFixture.get(anotherEmail, postId, 2L, commentId);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> commentService.edit(postId, commentId, "수정 댓글",email));
        assertThat(e.getErrorCode()).isEqualTo(INVALID_PERMISSION);
    }

    @DisplayName("댓글 삭제 요청 시 성공한 경우")
    @Test
    void deleteCommentTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";

        Long commentId = 1L;
        Comment comment = CommentFixture.get(anotherEmail, postId, 2L, commentId);
        User anotherUser = comment.getUser();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.delete(postId, commentId, anotherEmail));
    }

    @DisplayName("댓글 삭제 요청 시 댓글이 존재하지 않는 경우")
    @Test
    void deleteCommentExceptionTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";

        Long commentId = 1L;
        Comment comment = CommentFixture.get(anotherEmail, postId, 2L, commentId);
        User anotherUser = comment.getUser();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> commentService.delete(postId, commentId, anotherEmail));
        assertThat(e.getErrorCode()).isEqualTo(COMMENT_NOT_FOUND);
    }

    @DisplayName("댓글 삭제 요청 시 권한이 없는 경우")
    @Test
    void deleteCommentExceptionTest2() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";

        Long commentId = 1L;
        Comment comment = CommentFixture.get(anotherEmail, postId, 2L, commentId);
        User anotherUser = comment.getUser();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> commentService.delete(postId, commentId, email));
        assertThat(e.getErrorCode()).isEqualTo(INVALID_PERMISSION);
    }

}
