package com.project.sns.unit.service.auth;

import com.project.sns.exception.ErrorCode;
import com.project.sns.exception.SnsApplicationException;
import com.project.sns.fixture.UserFixture;
import com.project.sns.unit.UnitTest;
import com.project.sns.user.controller.dto.request.UserJoinRequest;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import com.project.sns.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Objects;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest extends UnitTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
        .getConnection()
        .flushAll();
    }

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

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.join(USER_JOIN_REQUEST));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_USER_EMAIL);
    }

    @Test
    @DisplayName("로그인이 정상적으로 동작하는 경우")
    void loginTest() {

        User fixture = UserFixture.get(USER_JOIN_REQUEST);

        when(userRepository.findByEmail(USER_JOIN_REQUEST.getEmail())).thenReturn(Optional.of(fixture));
        when(encoder.matches(USER_JOIN_REQUEST.getPassword(), fixture.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> userService.login(USER_JOIN_REQUEST.getEmail(), USER_JOIN_REQUEST.getPassword()));
    }

    @Test
    @DisplayName("로그인 시 해당 이메일로 회원가입한 유저가 없는 경우")
    void loginExceptionTest() {

        when(userRepository.findByEmail(USER_JOIN_REQUEST.getEmail())).thenReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(USER_JOIN_REQUEST.getEmail(), USER_JOIN_REQUEST.getPassword()));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("로그인 시 비밀번호가 틀린 경우")
    void loginExceptionTest2() {

        User fixture = UserFixture.get(USER_JOIN_REQUEST);

        when(userRepository.findByEmail(USER_JOIN_REQUEST.getEmail())).thenReturn(Optional.of(fixture));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(USER_JOIN_REQUEST.getEmail(), "wrong_password"));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.INVALID_PASSWORD);
    }
}
