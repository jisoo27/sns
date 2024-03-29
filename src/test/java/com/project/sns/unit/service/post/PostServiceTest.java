package com.project.sns.unit.service.post;

import com.project.sns.exception.SnsApplicationException;
import com.project.sns.fixture.LikeFixture;
import com.project.sns.fixture.PostFixture;
import com.project.sns.fixture.UserFixture;
import com.project.sns.image.domain.Image;
import com.project.sns.image.repository.ImageRepository;
import com.project.sns.like.domain.Like;
import com.project.sns.like.repository.LikeRepository;
import com.project.sns.post.domain.Post;
import com.project.sns.post.repository.PostRepository;
import com.project.sns.post.service.PostService;
import com.project.sns.unit.UnitTest;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static com.project.sns.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostServiceTest extends UnitTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private LikeRepository likeRepository;


    @DisplayName("게시글 등록이 성공한 경우")
    @Test
    void postSaveTest() {

        String email = "admin@email.com";
        String content = "내용이다";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(imageRepository.save(any())).thenReturn(mock(Image.class));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        assertDoesNotThrow(() -> postService.create(List.of("//"), content, email));
    }

    @DisplayName("게시글 등록한 유저가 없는 경우")
    @Test
    void postSaveExceptionTest() {

        String email = "admin@email.com";
        String content = "내용이다";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(imageRepository.save(any())).thenReturn(mock(Image.class));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.create(List.of("//"), content, email));
        assertThat(e.getErrorCode()).isEqualTo(USER_NOT_FOUND);
    }

    @DisplayName("게시글 수정이 성공한 경우")
    @Test
    void postEditTest() {

        String email = "admin@email.com";
        String content = "수정된 내용이다.";
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

    @DisplayName("게시물 삭제에 성공한 경우")
    @Test
    void postDeleteTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertDoesNotThrow(() -> postService.delete(email, postId));
    }

    @DisplayName("게시글 삭제 시 글이 존재하지 않는 경우")
    @Test
    void postDeleteExceptionTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.delete(email, postId));
        assertThat(e.getErrorCode()).isEqualTo(POST_NOT_FOUND);
    }

    @DisplayName("게시글 삭제 시 권한이 없는 경우")
    @Test
    void postDeleteExceptionTest2() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = UserFixture.get(email, "password", 2L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.delete(email, postId));
        assertThat(e.getErrorCode()).isEqualTo(INVALID_PERMISSION);
    }

    @DisplayName("좋아요 요청 시 성공한 경우")
    @Test
    void postLikeTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        String anotherEmail = "somin@email.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(mock(Post.class)));
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(mock(User.class)));

        assertDoesNotThrow(() -> postService.like(anotherEmail, postId));
    }

    @DisplayName("좋아요 요청 시 글이 존재하지 않는 경우")
    @Test
    void postLikeExceptionTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";
        User anotherUser = UserFixture.get(anotherEmail, "kkk", 2L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.like(anotherEmail, postId));
        assertThat(e.getErrorCode()).isEqualTo(POST_NOT_FOUND);
    }

    @DisplayName("좋아요 요청시 이미 좋아요 요청이 되어있는 경우")
    @Test
    void postLikeExceptionTest2() {
        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";
        User anotherUser = UserFixture.get(anotherEmail, "kkk", 2L);

        Like like = LikeFixture.get(anotherEmail, postId, anotherUser.getId(), 1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));
        when(likeRepository.findByUserAndPost(anotherUser, post)).thenReturn(Optional.of(like));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.like(anotherEmail, postId));
        assertThat(e.getErrorCode()).isEqualTo(ALREADY_LIKED);
    }

    @DisplayName("좋아요 취소 요청시 성공한 경우")
    @Test
    void postNotLikeTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";
        User anotherUser = UserFixture.get(anotherEmail, "kkk", 2L);
        Like like = LikeFixture.get(anotherEmail, postId, anotherUser.getId(), 1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));
        when(likeRepository.findByUserAndPost(anotherUser, post)).thenReturn(Optional.of(like));

        assertDoesNotThrow(() -> postService.notLike(anotherEmail, postId));
    }

    @DisplayName("좋아요 취소 요청시 글이 존재하지 않는 경우")
    @Test
    void postNotLikeExceptionTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";
        User anotherUser = UserFixture.get(anotherEmail, "kkk", 2L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.notLike(anotherEmail, postId));
        assertThat(e.getErrorCode()).isEqualTo(POST_NOT_FOUND);
    }

    @DisplayName("좋아요 취소 요청시, 좋아요가 되어있지 않으면 요청에 실패한다.")
    @Test
    void postNotLikeExceptionTest2() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";
        User anotherUser = UserFixture.get(anotherEmail, "kkk", 2L);
        Like like = LikeFixture.get(anotherEmail, postId, anotherUser.getId(), 1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));
        when(likeRepository.findByUserAndPost(anotherUser, post)).thenReturn(Optional.of(like));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.notLike(email, postId));
        assertThat(e.getErrorCode()).isEqualTo(LIKE_NOT_FOUND);
    }

    @DisplayName("자신이 좋아요 한 글 조회 요청 시 성공한 경우")
    @Test
    void getMyLikePostTest() {

        Pageable pageable = mock(Pageable.class);
        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        Long likeId = 1L;
        Like like = LikeFixture.get(email, postId, 1L, likeId);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(likeRepository.findById(likeId)).thenReturn(Optional.of(like));
        when(likeRepository.findAllByUser(user, pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> postService.getMyLikeList(user.getEmail(), pageable));
    }

    @DisplayName("좋아요 개수 조회 요청 시 성공한 경우")
    @Test
    void getPostLikeCountTest() {

        String email = "admin@email.com";
        Long postId = 1L;

        Post post = PostFixture.get(email, postId, 1L);
        User user = post.getUser();

        String anotherEmail = "somin@email.com";
        User anotherUser = UserFixture.get(anotherEmail, "kkk", 2L);
        Like like = LikeFixture.get(anotherEmail, postId, anotherUser.getId(), 1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(anotherEmail)).thenReturn(Optional.of(anotherUser));
        when(likeRepository.findByUserAndPost(anotherUser, post)).thenReturn(Optional.of(like));

        assertDoesNotThrow(() -> postService.getLikeCount(postId));
    }

    @DisplayName("모든 회원이 작성한 게시물 조회에 성공한 경우")
    @Test
    void getAllPostTest() {

        Pageable pageable = mock(Pageable.class);
        User user = mock(User.class);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(postRepository.findAll(pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> postService.getAllList(user.getEmail(), pageable));
    }

    @DisplayName("회원 나이 대 별 모아보기 요청에 성공한 경우")
    @Test
    void getUserAgeFilterTest() {

        Pageable pageable = mock(Pageable.class);
        User user = mock(User.class);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(postRepository.findAllByAge(pageable, 20, 30)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> postService.getAgeFilterList(user.getEmail(), 20, 30, pageable));
    }
}
