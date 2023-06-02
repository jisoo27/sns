package com.project.sns.unit.service;

import com.project.sns.exception.SnsApplicationException;
import com.project.sns.fixture.UserFixture;
import com.project.sns.user.controller.dto.request.UserJoinRequest;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import com.project.sns.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    private static UserJoinRequest USER_JOIN_REQUEST = UserJoinRequest.builder()
            .age(20)
            .email("admin@email.com")
            .password("password")
            .userName("userName")
            .nickName("nickName")
            .address("/")
            .profileMessage("hi")
            .profileImage("/")
            .build();

    @Test
    @DisplayName("회원가입이 정상적으로 동작하는 경우")
    void joinTest() {

        when(userRepository.findByEmail(USER_JOIN_REQUEST.getEmail())).thenReturn(Optional.empty());
        when(encoder.encode(USER_JOIN_REQUEST.getPassword())).thenReturn("encrypt_password");
        when(userRepository.save(any())).thenReturn(UserFixture.get(USER_JOIN_REQUEST));

        assertDoesNotThrow(() -> userService.join(USER_JOIN_REQUEST));
    }

    @Test
    @DisplayName("이미 가입된 이메일로 회원가입을 요청하는 경우")
    void joinExceptionTest() {

        User fixture = UserFixture.get(USER_JOIN_REQUEST);

        when(userRepository.findByEmail(USER_JOIN_REQUEST.getEmail())).thenReturn(Optional.of(fixture));
        when(encoder.encode(USER_JOIN_REQUEST.getPassword())).thenReturn("encrypt_password");
        when(userRepository.save(any())).thenReturn(Optional.of(mock(User.class)));

        assertThatThrownBy(() -> userService.join(USER_JOIN_REQUEST)).isInstanceOf(SnsApplicationException.class);
    }
}
