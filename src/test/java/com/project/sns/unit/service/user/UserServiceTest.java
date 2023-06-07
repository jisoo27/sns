package com.project.sns.unit.service.user;

import com.project.sns.exception.SnsApplicationException;
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


    @Test
    @DisplayName("로그인한 회원이 자신의 정보를 조회하는 경우")
    void getMyInfoTest() {

        String email = "admin@email.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        assertDoesNotThrow(() -> userService.getMyInformation(email));
    }

    @Test
    @DisplayName("존재하지 않는 유저가 자신의 정보를 조회할 경우")
    void getMyInfoExceptionTest() {

        String email = "admin@email.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.getMyInformation(email));
        assertThat(e.getErrorCode()).isEqualTo(USER_NOT_FOUND);
    }

}

