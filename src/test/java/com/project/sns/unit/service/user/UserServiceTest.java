package com.project.sns.unit.service.user;

import com.project.sns.exception.SnsApplicationException;
import com.project.sns.fixture.PostFixture;
import com.project.sns.post.domain.Post;
import com.project.sns.unit.UnitTest;
import com.project.sns.user.controller.dto.request.UserEditInfoRequest;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.AlarmRepository;
import com.project.sns.user.repository.UserRepository;
import com.project.sns.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import static com.project.sns.exception.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest extends UnitTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AlarmRepository alarmRepository;

    private final String EMAIL = "admin@email.com";


    @Test
    @DisplayName("로그인한 회원이 자신의 정보를 조회하는 경우")
    void getMyInfoTest() {

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(mock(User.class)));
        assertDoesNotThrow(() -> userService.getMyInformation(EMAIL));
    }

    @Test
    @DisplayName("존재하지 않는 유저가 자신의 정보를 조회할 경우")
    void getMyInfoExceptionTest() {

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.getMyInformation(EMAIL));
        assertThat(e.getErrorCode()).isEqualTo(USER_NOT_FOUND);
    }

    @Test
    @DisplayName("로그인한 회원이 자신의 정보를 수정하는 경우")
    void editMyInfoTest() {

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(mock(User.class)));
        assertDoesNotThrow(() -> userService.editMyInformation(EMAIL, new UserEditInfoRequest("hi", "//", "sona")));
    }

    @Test
    @DisplayName("존재하지 않는 유저가 자신의 정보를 수정할 경우")
    void editMyInfoExceptionTest() {

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.editMyInformation(EMAIL, new UserEditInfoRequest("hi", "//", "sona")));
        assertThat(e.getErrorCode()).isEqualTo(USER_NOT_FOUND);
    }

    @Test
    @DisplayName("댓글 알림 조회 요청에 성공할 경우")
    void getCommentAlarmListTest() {

        Long postId = 1L;
        Post post = PostFixture.get(EMAIL, postId, 1L);
        User user = post.getUser();

        Pageable pageable = mock(Pageable.class);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(alarmRepository.findAllByUserId(1L, pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> userService.getAlarmList(1L, pageable));
    }

    @Test
    @DisplayName("좋아요 알림 조회 요청에 성공할 경우")
    void getLikeAlarmListTest() {

        Long postId = 1L;
        Post post = PostFixture.get(EMAIL, postId, 1L);
        User user = post.getUser();

        Pageable pageable = mock(Pageable.class);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(alarmRepository.findAllByUserId(1L, pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> userService.getAlarmList(1L, pageable));
    }

}

