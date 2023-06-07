package com.project.sns.unit.service.user;

import com.project.sns.exception.SnsApplicationException;
import com.project.sns.user.controller.dto.request.UserEditInfoRequest;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import com.project.sns.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import static com.project.sns.exception.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

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

}

